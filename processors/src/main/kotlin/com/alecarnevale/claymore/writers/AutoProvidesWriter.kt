@file:OptIn(DelicateKotlinPoetApi::class)

package com.alecarnevale.claymore.writers

import com.alecarnevale.claymore.annotations.AutoBinds
import com.alecarnevale.claymore.api.AutoProvidesKeysProvider
import com.alecarnevale.claymore.utils.keyProviderQualifierAnnotation
import com.alecarnevale.claymore.utils.qualifierAnnotation
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.DelicateKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName
import javax.inject.Inject

/**
 *
 */
internal class AutoProvidesWriter() {

  fun writeAutoProvidesKeysProvider(
    activityDeclaration: KSClassDeclaration,
    autoQualifierDeclaration: KSClassDeclaration,
  ): FileSpec {
    val className = "${activityDeclaration.toClassName().simpleName}_AutoProvidesKeysProvider"

    val fileSpec = FileSpec.builder(
      packageName = activityDeclaration.toClassName().packageName,
      fileName = className
    )

    val autoBindsAnnotation =
      AnnotationSpec
        .builder(AutoBinds::class.java)
        .addMember(CodeBlock.of("annotations = [%L::class]", autoQualifierDeclaration.toClassName().simpleName))
        .build()

    val injectAnnotation =
      AnnotationSpec
        .builder(Inject::class.java)
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
        .build()
    ).build()
  }
}