package com.alecarnevale.claymore.visitors

import com.alecarnevale.claymore.annotations.InterfaceAutoBinds
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

    // extract the KSType of arguments
    val arguments = classDeclaration.annotations.iterator().next().arguments
    val implementationKsType = arguments.firstOrNull { it.name?.getShortName() == InterfaceAutoBinds::implementation.name }?.value as? KSType
    if (implementationKsType == null) {
      logger.error("$TAG implementation class must be provided")
      return
    }
    val componentKsType = arguments.firstOrNull { it.name?.getShortName() == InterfaceAutoBinds::component.name }?.value as? KSType
    if (componentKsType == null) {
      logger.error("$TAG component class not found")
      return
    }
    logger.info("$TAG implementation argument provided is $implementationKsType")
    logger.info("$TAG component argument provided is $componentKsType")

    // extract the KSName of arguments
    val implementationQualifiedName = implementationKsType.declaration.qualifiedName
    if (implementationQualifiedName == null) {
      logger.error("$TAG qualified name is null")
      return
    }
    val componentQualifiedName = componentKsType.declaration.qualifiedName
    if (componentQualifiedName == null) {
      logger.error("$TAG qualified name is null")
      return
    }

    // extract the KSClassDeclaration of arguments
    val implementationProvided = resolver.getClassDeclarationByName(implementationQualifiedName)
    if (implementationProvided == null) {
      logger.error("$TAG implementation class not found")
      return
    }
    val componentProvided = resolver.getClassDeclarationByName(componentQualifiedName)
    if (componentProvided == null) {
      logger.error("$TAG implementation class not found")
      return
    }

    // check if the implementation class provided implements the interface
    val superTypes = implementationProvided.superTypes.map { it.resolve() }
    if (classDeclaration !in superTypes.map { it.declaration }) {
      logger.error("$TAG $classDeclaration is not a superType of $implementationProvided")
      return
    }

    // define the sources file that generated the module
    val interfaceSourceFile = classDeclaration.containingFile
    if (interfaceSourceFile == null) {
      logger.error("$TAG can not find source file of $classDeclaration")
      return
    }
    val implementationSourceFile = implementationProvided.containingFile
    if (implementationSourceFile == null) {
      logger.error("$TAG can not find source file of $implementationProvided")
      return
    }

    // this is and isolating output, since no new change on other files will affect this
    val writer = ModuleWriter(
      interfaceDeclaration = classDeclaration,
      implementationDeclaration = implementationProvided,
      componentDeclaration = componentProvided
    )
    writer.write().writeTo(
      codeGenerator = codeGenerator,
      aggregating = false,
      originatingKSFiles = listOf(interfaceSourceFile, implementationSourceFile)
    )
  }
}

private const val TAG = "Claymore - InterfaceAutoBindsVisitor:"
