package com.alecarnevale.claymore.generator

import com.alecarnevale.claymore.Constants
import com.alecarnevale.claymore.generator.utils.bindsAnnotation
import com.alecarnevale.claymore.generator.utils.installInAnnotation
import com.alecarnevale.claymore.generator.utils.moduleAnnotation
import com.alecarnevale.claymore.generator.utils.moduleClassName
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName

/**
 * Write a hilt module that binds [implementationDeclaration] for [interfaceDeclaration].
 */
internal class ModuleWriter(
  private val interfaceDeclaration: KSClassDeclaration,
  private val implementationDeclaration: KSClassDeclaration
) {

  fun write(): FileSpec {
    val fileSpec = FileSpec.builder(
      packageName = Constants.packageName,
      fileName = interfaceDeclaration.moduleClassName()
    )

    val functionName =
      interfaceDeclaration.simpleName.asString().replaceFirstChar { it.lowercase() }

    return fileSpec.addType(
      TypeSpec
        .interfaceBuilder(interfaceDeclaration.moduleClassName())
        .addModifiers(KModifier.INTERNAL)
        .addAnnotation(moduleAnnotation)
        .addAnnotation(installInAnnotation)
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