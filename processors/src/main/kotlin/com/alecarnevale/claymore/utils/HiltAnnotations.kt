package com.alecarnevale.claymore.utils

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.multibindings.IntoSet

private val module = Module::class.java.toClassName()
private val installIn = InstallIn::class.java.toClassName()
private val binds = Binds::class.java.toClassName()
private val intoSet = IntoSet::class.java.toClassName()

internal val moduleAnnotation = AnnotationSpec
  .builder(module)
  .build()

internal fun installInAnnotation(component: KSClassDeclaration) = AnnotationSpec
  .builder(installIn)
  .addMember("%M::class", component.toMemberName())
  .build()

internal val bindsAnnotation = AnnotationSpec
  .builder(binds)
  .build()

private fun KSClassDeclaration.toMemberName(): MemberName =
  MemberName(packageName = packageName.asString(), simpleName = simpleName.asString())

private fun Class<*>.toClassName(): ClassName =
  ClassName(packageName = packageName, simpleName)