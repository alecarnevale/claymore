package com.alecarnevale.claymore.visitor

import com.alecarnevale.claymore.annotation.InterfaceAutoBinds
import com.alecarnevale.claymore.generator.ModuleWriter
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * This visitor check if provided implementation of [InterfaceAutoBinds] is a descendant of the annotated interface.
 * If true it generates the necessary hilt module.
 */
class InterfaceAutoBindsVisitor(
  private val codeGenerator: CodeGenerator,
  private val resolver: Resolver,
  private val logger: KSPLogger
) : KSVisitorVoid() {

  override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
    logger.info("$TAG visitClassDeclaration of $classDeclaration")
    val arguments = classDeclaration.annotations.iterator().next().arguments
    val ksType = arguments[0].value as KSType
    logger.info("$TAG first argument provided is $ksType")

    val qualifiedName = ksType.declaration.qualifiedName
    if (qualifiedName == null) {
      logger.error("$TAG qualified name is null")
      return
    }

    val classImplementationProvided = resolver.getClassDeclarationByName(qualifiedName)
    if (classImplementationProvided == null) {
      logger.error("$TAG implementation class not found")
      return
    }

    val superTypes = classImplementationProvided.superTypes.map { it.resolve() }
    if (classDeclaration !in superTypes.map { it.declaration }) {
      logger.error("$TAG $classDeclaration is not a superType of $classImplementationProvided")
      return
    }

    val interfaceSourceFile = classDeclaration.containingFile
    if (interfaceSourceFile == null) {
      logger.error("$TAG can not find source file of $classDeclaration")
      return
    }
    val implementationSourceFile = classImplementationProvided.containingFile
    if (implementationSourceFile == null) {
      logger.error("$TAG can not find source file of $classImplementationProvided")
      return
    }

    // this is and isolating output, since no new change on other files will affect this
    val writer = ModuleWriter(classDeclaration, classImplementationProvided)
    writer.write().writeTo(
      codeGenerator = codeGenerator,
      aggregating = false,
      originatingKSFiles = listOf(interfaceSourceFile, implementationSourceFile)
    )
  }
}

private const val TAG = "Claymore - InterfaceAutoBindsVisitor:"
