package com.alecarnevale.claymore.utils

import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.multibindings.IntoSet

private val module = Module::class.java.toClassName()
private val installIn = InstallIn::class.java.toClassName()
private val binds = Binds::class.java.toClassName()
private val provides = Provides::class.java.toClassName()

internal val moduleAnnotation = AnnotationSpec
  .builder(module)
  .build()

internal fun installInAnnotation(component: KSClassDeclaration) = AnnotationSpec
  .builder(installIn)
  .addMember("%M::class", component.toMemberName())
  .build()

internal fun installInAnnotation(component: ClassName) = AnnotationSpec
  .builder(installIn)
  .addMember("%M::class", component.toMemberName())
  .build()

internal val bindsAnnotation = AnnotationSpec
  .builder(binds)
  .build()

internal val providesAnnotation = AnnotationSpec
  .builder(provides)
  .build()

private fun KSClassDeclaration.toMemberName(): MemberName =
  MemberName(packageName = packageName.asString(), simpleName = simpleName.asString())

private fun ClassName.toMemberName(): MemberName =
  MemberName(packageName = packageName, simpleName = simpleName)

private fun Class<*>.toClassName(): ClassName =
  ClassName(packageName = packageName, simpleName)