package com.alecarnevale.claymore.visitors

import com.alecarnevale.claymore.generator.ModuleWriter
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * This visitor check if the class implements exactly an interface.
 * If true it generates the necessary hilt module to bind that class as the actual implementation.
 */
class AutoBindsVisitor(
  private val codeGenerator: CodeGenerator,
  private val resolver: Resolver,
  private val logger: KSPLogger
) : KSVisitorVoid() {

  override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
    logger.info("$TAG visitClassDeclaration of $classDeclaration")

    val superTypes = classDeclaration.superTypes
    val superType = when (superTypes.count()) {
      0 -> {
        logger.error("$TAG $classDeclaration musts implement an interface.")
        return
      }

      1 -> {
        superTypes.single()
      }

      else -> {
        logger.error("$TAG $classDeclaration musts implement at most one interface.")
        return
      }
    }

    val interfaceDeclaration =
      superType.resolve().declaration.qualifiedName?.let { resolver.getClassDeclarationByName(it) }
    if (interfaceDeclaration == null || interfaceDeclaration.classKind != ClassKind.INTERFACE) {
      logger.error("$TAG expecting $interfaceDeclaration as an interface.")
      return
    }

    val interfaceSourceFile = interfaceDeclaration.containingFile
    if (interfaceSourceFile == null) {
      logger.error("$TAG can not find source file of $interfaceDeclaration")
      return
    }
    val implementationSourceFile = classDeclaration.containingFile
    if (implementationSourceFile == null) {
      logger.error("$TAG can not find source file of $classDeclaration")
      return
    }

    // this is and isolating output, since no new change on other files will affect this
    val writer = ModuleWriter(interfaceDeclaration, classDeclaration)
    writer.write().writeTo(
      codeGenerator = codeGenerator,
      aggregating = false,
      originatingKSFiles = listOf(interfaceSourceFile, implementationSourceFile)
    )
  }
}

private const val TAG = "Claymore - AutoBindsVisitor:"