package com.alecarnevale.claymore.utils

import com.alecarnevale.claymore.annotations.keyprovider.KeyProviderQualifier
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import javax.inject.Qualifier

private val qualifier = Qualifier::class.java.toClassName()
private val keyProviderQualifier = KeyProviderQualifier::class.java.toClassName()

internal val qualifierAnnotation = AnnotationSpec
  .builder(qualifier)
  .build()

internal fun keyProviderQualifierAnnotation(activityClassName: String) = AnnotationSpec
  .builder(keyProviderQualifier)
  .addMember(CodeBlock.of("%L::class", activityClassName))
  .build()

private fun Class<*>.toClassName(): ClassName =
  ClassName(packageName = packageName, simpleName)