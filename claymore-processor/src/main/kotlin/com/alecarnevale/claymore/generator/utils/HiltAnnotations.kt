package com.alecarnevale.claymore.generator.utils

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.MemberName

private val module = ClassName("dagger", "Module")
private val singletonComponent = MemberName("dagger.hilt.components", "SingletonComponent")
private val installIn = ClassName("dagger.hilt", "InstallIn")
private val binds = ClassName("dagger", "Binds")

internal val moduleAnnotation = AnnotationSpec
  .builder(module)
  .build()
internal val installInAnnotation = AnnotationSpec
  .builder(installIn)
  .addMember("%M::class", singletonComponent)
  .build()
internal val bindsAnnotation = AnnotationSpec
  .builder(binds)
  .build()