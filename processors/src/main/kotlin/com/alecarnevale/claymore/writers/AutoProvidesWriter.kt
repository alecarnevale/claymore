@file:OptIn(DelicateKotlinPoetApi::class)

package com.alecarnevale.claymore.writers

import com.alecarnevale.claymore.annotations.AutoBinds
import com.alecarnevale.claymore.annotations.keyprovider.AutoProvidesKeysProvider
import com.google.devtools.ksp.symbol.KSAnnotation
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSValueParameter
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.ksp.toClassName
import javax.inject.Inject
import kotlin.reflect.KClass

internal class AutoProvidesWriter() {

  /**
   * Generate the _AutoProvidesKeysProvider implementation of [AutoProvidesKeysProvider],
   * that override the get method to retrieve the key for a given annotation.
   */
  fun writeAutoProvidesKeysProvider(
    activityIntentDeclaration: KSClassDeclaration,
    autoQualifierDeclaration: KSClassDeclaration,
    parameters: List<KSValueParameter>
  ): FileSpec {
    val className = "${activityIntentDeclaration.toClassName().simpleName}_AutoProvidesKeysProvider"

    val fileSpec = FileSpec.builder(
      packageName = activityIntentDeclaration.toClassName().packageName,
      fileName = className
    )

    val autoBindsAnnotation =
      AnnotationSpec
        .builder(AutoBinds::class.java)
        .addMember(
          CodeBlock.of(
            "annotations = [%L::class]",
            autoQualifierDeclaration.toClassName().simpleName
          )
        )
        .build()

    val injectAnnotation =
      AnnotationSpec
        .builder(Inject::class.java)
        .build()

    val companionObject =
      TypeSpec
        .companionObjectBuilder()
        .addModifiers(KModifier.PRIVATE)
        .apply {
          parameters.forEach { parameter ->
            addProperty(
              PropertySpec
                .builder(parameter.name!!.getShortName(), String::class)
                .addModifiers(KModifier.PRIVATE)
                .addModifiers(KModifier.CONST)
                .initializer("\"${className}_${parameter.name!!.getShortName()}\"")
                .build()
            )
          }
        }
        .build()

    // long form to get -> KClass<out Annotation>
    val annotationType = KClass::class.asClassName().parameterizedBy(
      WildcardTypeName.producerOf(
        ClassName("kotlin", "Annotation")
      )
    )
    val getFunction =
      FunSpec
        .builder("get")
        .addModifiers(KModifier.OVERRIDE)
        .addModifiers(KModifier.OPERATOR)
        .addParameter(
          ParameterSpec
            .builder(name = "annotation", type = annotationType)
            .build()
        )
        .returns(String::class)
        .beginControlFlow("return when(annotation)")
        .apply {
          parameters.forEach {
            addStatement(
              "%M::class -> %L",
              it.annotations.first().asMemberName(),
              it.name!!.getShortName()
            )
          }
          addStatement("else -> throw Exception(\"Unexpected Annotation\")")
        }
        .endControlFlow()
        .build()

    return fileSpec.addType(
      TypeSpec
        .classBuilder(className)
        .addModifiers(KModifier.INTERNAL)
        .addAnnotation(autoBindsAnnotation)
        .addSuperinterface(AutoProvidesKeysProvider::class.java)
        .primaryConstructor(
          FunSpec.constructorBuilder().addAnnotation(injectAnnotation).build()
        )
        .addFunction(getFunction)
        .addType(companionObject)
        .build()
    ).build()
  }

  private fun KSAnnotation.asMemberName(): MemberName =
    annotationType.resolve().toClassName().let {
      MemberName(it.enclosingClassName()!!, it.simpleName)
    }
}