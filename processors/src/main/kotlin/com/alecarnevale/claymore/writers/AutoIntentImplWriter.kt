@file:OptIn(DelicateKotlinPoetApi::class)

package com.alecarnevale.claymore.writers

import com.alecarnevale.claymore.annotations.AutoBinds
import com.alecarnevale.claymore.annotations.AutoProvides
import com.alecarnevale.claymore.annotations.keyprovider.AutoProvidesKeysProvider
import com.alecarnevale.claymore.utils.applicationContext
import com.alecarnevale.claymore.utils.context
import com.alecarnevale.claymore.utils.intent
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import com.squareup.kotlinpoet.ksp.toTypeName
import javax.inject.Inject

/**
 * Generate the _AutoIntentImpl implementation of the interface annotated with [AutoProvides] annotation,
 * that override the invoke method to create the Intent with the given parameters.
 */
internal class AutoIntentImplWriter {

  fun write(
    activityIntentDeclaration: KSClassDeclaration,
    autoQualifierDeclaration: KSClassDeclaration,
    parameters: List<KSValueParameter>
  ): FileSpec {
    println(autoQualifierDeclaration)
    val className = "${activityIntentDeclaration.toClassName().simpleName}_AutoIntentImpl"

    val fileSpec = FileSpec.builder(
      packageName = activityIntentDeclaration.toClassName().packageName,
      fileName = className
    )

    val injectAnnotation =
      AnnotationSpec
        .builder(Inject::class.java)
        .build()

    val autoBindsAnnotation =
      AnnotationSpec
        .builder(AutoBinds::class.java)
        .build()

    val invokeFunction =
      FunSpec
        .builder("invoke")
        .addModifiers(KModifier.OVERRIDE)
        .addModifiers(KModifier.OPERATOR)
        .apply {
          parameters.forEach { param ->
            addParameter(
              param.name!!.asString(),
              param.type.toTypeName()
            )
          }
        }
        .returns(intent)
        .build()

    val autoProvidesKeysProvider =
      PropertySpec
        .builder("autoProvidesKeysProvider", AutoProvidesKeysProvider::class)
        .addAnnotation(injectAnnotation)
        .addAnnotation(autoQualifierDeclaration.toClassName())
        .addModifiers(KModifier.LATEINIT)
        .addModifiers(KModifier.INTERNAL)
        .mutable(true)
        .build()

    val applicationContext =
      PropertySpec
        .builder("context", context)
        .addAnnotation(injectAnnotation)
        .addAnnotation(applicationContext)
        .addModifiers(KModifier.LATEINIT)
        .addModifiers(KModifier.INTERNAL)
        .mutable(true)
        .build()

    return fileSpec
      .addType(
        TypeSpec
          .classBuilder(className)
          .addModifiers(KModifier.INTERNAL)
          .primaryConstructor(
            FunSpec
              .constructorBuilder()
              .addAnnotation(injectAnnotation)
              .build()
          )
          .addAnnotation(autoBindsAnnotation)
          .addSuperinterface(activityIntentDeclaration.toClassName())
          .addProperties(listOf(autoProvidesKeysProvider, applicationContext))
          .addFunction(invokeFunction)
          .build()
      )
      .build()
  }

}