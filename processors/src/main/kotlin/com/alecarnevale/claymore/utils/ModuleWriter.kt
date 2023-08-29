package com.alecarnevale.claymore.utils

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName

/**
 * Write a hilt module that binds [implementationDeclaration] for [interfaceDeclaration],
 * the binds is installed in [componentDeclaration].
 */
internal class ModuleWriter(
  private val interfaceDeclaration: KSClassDeclaration,
  private val implementationDeclaration: KSClassDeclaration,
  private val componentDeclaration: KSClassDeclaration
) {

  fun write(): FileSpec {
    val fileSpec = FileSpec.builder(
      packageName = implementationDeclaration.toClassName().packageName,
      fileName = implementationDeclaration.moduleClassName()
    )

    val functionName =
      interfaceDeclaration.simpleName.asString().replaceFirstChar { it.lowercase() }

    return fileSpec.addType(
      TypeSpec
        .interfaceBuilder(implementationDeclaration.moduleClassName())
        .addModifiers(KModifier.INTERNAL)
        .addAnnotation(moduleAnnotation)
        .addAnnotation(installInAnnotation(componentDeclaration))
        .addFunction(
          FunSpec.builder(functionName)
            .addAnnotation(bindsAnnotation)
            .addModifiers(KModifier.ABSTRACT)
            .addParameter("impl", implementationDeclaration.toClassName())
            .returns(interfaceDeclaration.toClassName())
            .build()
        ).build()
    ).build()
  }
}