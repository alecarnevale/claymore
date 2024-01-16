package com.alecarnevale.claymore.visitors

import com.alecarnevale.claymore.annotations.AutoBinds
import com.alecarnevale.claymore.annotations.AutoUninstall
import com.alecarnevale.claymore.utils.TestModuleWriter
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.ksp.writeTo
import kotlin.reflect.KClass

/**
 * This visitor check if implementations provided are concrete classes.
 * If true it generates a hilt test module that replaces all modules for implementations that
 * has been already generated by [AutoBinds] annotation.
 */
internal class AutoUninstallVisitor(
  override val codeGenerator: CodeGenerator,
  override val resolver: Resolver,
  override val logger: KSPLogger
) : Visitor() {

  override val kclass: KClass<*> = AutoUninstall::class

  override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
    logger.info("$TAG visitClassDeclaration of $classDeclaration")

    // extract the KSClassDeclaration of the implementations to uninstall
    val implementationsProvided =
      classDeclaration.extractParameters(AutoUninstall::implementations.name) ?: return
    implementationsProvided.forEach { implementation ->
      (implementation.classKind != ClassKind.CLASS || implementation.modifiers.contains(Modifier.ABSTRACT)).also { notImplementation ->
        if (notImplementation) {
          logger.error("$TAG $implementation is not a valid concrete class")
          return
        }
      }
    }

    // extract the KSClassDeclaration of the component where to install the module
    val componentsProvided =
      classDeclaration.extractParameters(AutoUninstall::components.name) ?: return

    // define the sources file that generated the module
    val implementationSourceFile = classDeclaration.containingFile
    if (implementationSourceFile == null) {
      logger.error("$TAG can not find source file of $classDeclaration")
      return
    }

    val name = classDeclaration.simpleName.getShortName().plus("_AutoUninstallModule")
    val packageName = classDeclaration.packageName.asString()

    val dependencies = listOf(implementationSourceFile)

    // this is an isolating output, since no new change on others files will affect this
    val writer = TestModuleWriter(
      name = name,
      packageName = packageName,
      components = componentsProvided,
      implementationsDeclaration = implementationsProvided,
    )
    writer.write().writeTo(
      codeGenerator = codeGenerator,
      aggregating = false,
      originatingKSFiles = dependencies
    )
  }

}

private const val TAG = "Claymore - AutoUninstallVisitor:"