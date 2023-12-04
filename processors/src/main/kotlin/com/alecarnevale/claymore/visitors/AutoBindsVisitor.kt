package com.alecarnevale.claymore.visitors

import com.alecarnevale.claymore.annotations.AutoBinds
import com.alecarnevale.claymore.utils.ModuleWriter
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.ksp.writeTo
import kotlin.reflect.KClass

/**
 * This visitor check if the class implements exactly an interface.
 * If true it generates the necessary hilt module to bind that class as the actual implementation.
 */
internal class AutoBindsVisitor(
  override val codeGenerator: CodeGenerator,
  override val resolver: Resolver,
  override val logger: KSPLogger
) : Visitor() {

  override val kclass: KClass<*> = AutoBinds::class

  override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
    logger.info("$TAG visitClassDeclaration of $classDeclaration")

    // get the KSClassDeclaration of the interface implemented
    val interfaceDeclaration =
      classDeclaration.superTypes.single().resolve().declaration.qualifiedName?.let { resolver.getClassDeclarationByName(it) }
    if (interfaceDeclaration == null || interfaceDeclaration.isNotValidSupertype()) {
      logger.error("$TAG expecting $interfaceDeclaration as an interface or abstract class.")
      return
    }

    // extract the KSClassDeclaration of the arguments
    val componentProvided = classDeclaration.extractParameter(AutoBinds::component.name) ?: return
    val intoSet = classDeclaration.extractBooleanParameter(AutoBinds::intoSet.name) ?: return

    // define the sources file that generated the module
    val implementationSourceFile = classDeclaration.containingFile
    if (implementationSourceFile == null) {
      logger.error("$TAG can not find source file of $classDeclaration")
      return
    }
    // since the interface class file is a transitive dependency of the implementation class file,
    // it doesn't need to be specified as dependency (originatingKSFiles)
    val dependencies = listOf(implementationSourceFile)

    // this is and isolating output, since no new change on other files will affect this
    val writer = ModuleWriter(
      interfaceDeclaration = interfaceDeclaration,
      implementationDeclaration = classDeclaration,
      componentDeclaration = componentProvided,
      intoSet = intoSet,
    )
    writer.write().writeTo(
      codeGenerator = codeGenerator,
      aggregating = false,
      originatingKSFiles = dependencies
    )
  }

  private fun KSClassDeclaration.isNotValidSupertype(): Boolean = !isValidSupertype()

  private fun KSClassDeclaration.isValidSupertype(): Boolean {
    if (classKind == ClassKind.INTERFACE) {
      return true
    }
    if (classKind == ClassKind.CLASS && this.modifiers.contains(Modifier.ABSTRACT)) {
      return true
    }
    return false
  }
}

private const val TAG = "Claymore - AutoBindsVisitor:"