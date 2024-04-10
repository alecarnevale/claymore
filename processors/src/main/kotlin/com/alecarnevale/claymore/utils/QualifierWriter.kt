package com.alecarnevale.claymore.utils

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toClassName

/**
 * Generate the MyActivity_AutoQualifier annotation starting from [activityDeclaration].
 */
internal class QualifierWriter(
  private val activityDeclaration: KSClassDeclaration,
) {

  fun write(): FileSpec {
    val qualifierName = "${activityDeclaration.toClassName().simpleName}_AutoQualifier"

    val fileSpec = FileSpec.builder(
      packageName = activityDeclaration.toClassName().packageName,
      fileName = qualifierName
    )

    return fileSpec.addType(
      TypeSpec
        .annotationBuilder(qualifierName)
        .addModifiers(KModifier.INTERNAL)
        .addAnnotation(qualifierAnnotation)
        .addAnnotation(keyProviderQualifierAnnotation(activityDeclaration.toClassName().simpleName))
        .build()
    ).build()
  }
}