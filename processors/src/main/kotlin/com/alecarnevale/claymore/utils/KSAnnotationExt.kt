package com.alecarnevale.claymore.utils

import com.google.devtools.ksp.symbol.KSAnnotation
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ksp.toClassName

internal fun KSAnnotation.asMemberName(): MemberName =
  annotationType.resolve().toClassName().let {
    MemberName(it.enclosingClassName()!!, it.simpleName)
  }