package com.alecarnevale.claymore.visitors

import com.alecarnevale.claymore.annotations.InterfaceAutoBinds
import com.alecarnevale.claymore.utils.ModuleWriter
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * This visitor check if provided implementation of [InterfaceAutoBinds] is a descendant of the annotated interface.
 * If true it generates the necessary hilt module.
 */
internal class InterfaceAutoBindsVisitor(
  override val codeGenerator: CodeGenerator,
  override val resolver: Resolver,
  override val logger: KSPLogger
) : Visitor() {

  override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
    logger.info("$TAG visitClassDeclaration of $classDeclaration")

    // extract the KSClassDeclaration of the arguments
    val implementationProvided =
      classDeclaration.extractParameter(InterfaceAutoBinds::implementation.name) ?: return
    val componentProvided =
      classDeclaration.extractParameter(InterfaceAutoBinds::component.name) ?: return

    if (implementationProvided.modifiers.contains(Modifier.ABSTRACT)) {
      logger.error("$TAG implementation class must not be abstract")
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
    // since the implementation class file is a transitive dependency of the interface class file,
    // it doesn't need to be specified as dependency (originatingKSFiles)
    val dependencies = listOf(interfaceSourceFile)

    // this is and isolating output, since no new change on other files will affect this
    val writer = ModuleWriter(
      interfaceDeclaration = classDeclaration,
      implementationDeclaration = implementationProvided,
      componentDeclaration = componentProvided
    )
    writer.write().writeTo(
      codeGenerator = codeGenerator,
      aggregating = false,
      originatingKSFiles = dependencies
    )
  }

  private fun KSClassDeclaration.extractParameter(parameterName: String): KSClassDeclaration? {
    // extract the KSType
    val autobindsAnnotation =
      annotations.firstOrNull { it.shortName.getShortName() == InterfaceAutoBinds::class.simpleName }
    val parameterKsType =
      autobindsAnnotation?.arguments?.firstOrNull { it.name?.getShortName() == parameterName }?.value as? KSType
    if (parameterKsType == null) {
      logger.error("$TAG parameter class not found for $parameterName")
      return null
    }

    // extract the KSName
    val parameterQualifiedName = parameterKsType.declaration.qualifiedName
    if (parameterQualifiedName == null) {
      logger.error("$TAG qualified name is null for $parameterName")
      return null
    }

    // extract the KSClassDeclaration
    val parameterDeclaration = resolver.getClassDeclarationByName(parameterQualifiedName)
    if (parameterDeclaration == null) {
      logger.error("$TAG implementation class not found for $parameterName")
      return null
    }

    return parameterDeclaration
  }
}

private const val TAG = "Claymore - InterfaceAutoBindsVisitor:"
