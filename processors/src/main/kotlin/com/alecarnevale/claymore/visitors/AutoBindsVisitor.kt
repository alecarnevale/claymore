package com.alecarnevale.claymore.visitors

import com.alecarnevale.claymore.annotations.AutoBinds
import com.alecarnevale.claymore.utils.ModuleWriter
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.Modifier
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * This visitor check if the class implements exactly an interface.
 * If true it generates the necessary hilt module to bind that class as the actual implementation.
 */
internal class AutoBindsVisitor(
  override val codeGenerator: CodeGenerator,
  override val resolver: Resolver,
  override val logger: KSPLogger
) : Visitor() {

  override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
    logger.info("$TAG visitClassDeclaration of $classDeclaration")

    // check that the class implements an interface only
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

    // get the KSClassDeclaration of the interface implemented
    val interfaceDeclaration =
      superType.resolve().declaration.qualifiedName?.let { resolver.getClassDeclarationByName(it) }
    if (interfaceDeclaration == null || interfaceDeclaration.isNotValidSupertype()) {
      logger.error("$TAG expecting $interfaceDeclaration as an interface or abstract class.")
      return
    }

    // extract the KSClassDeclaration of the arguments
    val componentProvided = classDeclaration.extractParameter(AutoBinds::component.name) ?: return

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
      componentDeclaration = componentProvided
    )
    writer.write().writeTo(
      codeGenerator = codeGenerator,
      aggregating = false,
      originatingKSFiles = dependencies
    )
  }

  private fun KSClassDeclaration.isNotValidSupertype(): Boolean = !isValidSupertype()

  private fun KSClassDeclaration.extractParameter(parameterName: String): KSClassDeclaration? {
    // extract the KSType
    val autobindsAnnotation =
      annotations.firstOrNull { it.shortName.getShortName() == AutoBinds::class.simpleName }
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