package com.alecarnevale.claymore.writers

import com.alecarnevale.claymore.utils.bindsAnnotation
import com.alecarnevale.claymore.utils.installInAnnotation
import com.alecarnevale.claymore.utils.moduleAnnotation
import com.alecarnevale.claymore.utils.moduleClassName
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName

/**
 * Write a hilt module that binds [implementationDeclaration] for [interfaceDeclaration],
 * the binds is installed in [componentDeclaration].
 * Attach each annotation provided in [annotationsDeclaration] to the generated function.
 */
internal class ModuleWriter(
  private val interfaceDeclaration: KSClassDeclaration,
  private val implementationDeclaration: KSClassDeclaration,
  private val componentDeclaration: KSClassDeclaration,
  private val annotationsDeclaration: List<KSClassDeclaration>,
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
            .apply {
              annotationsDeclaration.forEach { annotation ->
                addAnnotation(annotation.toClassName())
              }
            }
            .addModifiers(KModifier.ABSTRACT)
            .addParameter("impl", implementationDeclaration.toClassName())
            .returns(interfaceDeclaration.toClassName())
            .build()
        ).build()
    ).build()
  }
}