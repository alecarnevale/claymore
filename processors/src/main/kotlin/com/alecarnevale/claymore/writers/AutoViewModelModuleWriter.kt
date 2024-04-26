package com.alecarnevale.claymore.writers

import com.alecarnevale.claymore.annotations.keyprovider.AutoProvidesKeysProvider
import com.alecarnevale.claymore.utils.asMemberName
import com.alecarnevale.claymore.utils.installInAnnotation
import com.alecarnevale.claymore.utils.moduleAnnotation
import com.alecarnevale.claymore.utils.providesAnnotation
import com.alecarnevale.claymore.utils.savedStateHandle
import com.alecarnevale.claymore.utils.viewModelComponent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toAnnotationSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName

/**
 * Generate the _AutoViewModelModule that provides the parameters into the the ViewModelScope.
 */
internal class AutoViewModelModuleWriter {

  fun write(
    activityIntentDeclaration: KSClassDeclaration,
    autoQualifierDeclaration: KSClassDeclaration,
    parameters: List<KSValueParameter>
  ): FileSpec {
    val className = "${activityIntentDeclaration.toClassName().simpleName}_AutoViewModelModule"

    val fileSpec = FileSpec.builder(
      packageName = activityIntentDeclaration.toClassName().packageName,
      fileName = className
    )

    val providesFunctions =
      parameters.map { param ->
        val qualifier = param.annotations.first()
        FunSpec
          .builder(param.name!!.asString())
          .addModifiers(KModifier.INTERNAL)
          .addAnnotation(providesAnnotation)
          .addAnnotation(qualifier.toAnnotationSpec())
          .addParameter(
            ParameterSpec
              .builder("handle", savedStateHandle)
              .build()
          )
          .addParameter(
            ParameterSpec
              .builder("autoProvidesKeysProvider", AutoProvidesKeysProvider::class.java)
              .addAnnotation(autoQualifierDeclaration.toClassName())
              .build()
          )
          .addStatement(
            "return requireNotNull(handle[autoProvidesKeysProvider[%M::class]])",
            qualifier.asMemberName(),
          )
          .returns(param.type.toTypeName())
          .build()
      }

    return fileSpec.addType(
      TypeSpec
        .classBuilder(className)
        .addModifiers(KModifier.INTERNAL)
        .addAnnotations(listOf(moduleAnnotation, installInAnnotation(viewModelComponent)))
        .addFunctions(providesFunctions.asIterable())
        .build()
    ).build()
  }
}