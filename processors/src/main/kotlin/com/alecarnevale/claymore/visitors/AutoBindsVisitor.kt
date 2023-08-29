package com.alecarnevale.claymore.visitors

import com.alecarnevale.claymore.annotations.AutoBinds
import com.alecarnevale.claymore.utils.ModuleWriter
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
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
    if (interfaceDeclaration == null || interfaceDeclaration.classKind != ClassKind.INTERFACE) {
      logger.error("$TAG expecting $interfaceDeclaration as an interface.")
      return
    }

    // extract the KSType of the component argument
    val arguments = classDeclaration.annotations.iterator().next().arguments
    val componentKsType = arguments.firstOrNull { it.name?.getShortName() == AutoBinds::component.name }?.value as? KSType
    if (componentKsType == null) {
      logger.error("$TAG component class not found")
      return
    }
    logger.info("$TAG component argument provided is $componentKsType")

    // extract the KSName of the component argument
    val componentQualifiedName = componentKsType.declaration.qualifiedName
    if (componentQualifiedName == null) {
      logger.error("$TAG qualified name is null")
      return
    }

    // extract the KSClassDeclaration of the component argument
    val componentProvided = resolver.getClassDeclarationByName(componentQualifiedName)
    if (componentProvided == null) {
      logger.error("$TAG implementation class not found")
      return
    }

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
}

private const val TAG = "Claymore - AutoBindsVisitor:"