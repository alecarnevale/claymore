package com.alecarnevale.claymore.utils

import com.google.devtools.ksp.symbol.KSDeclaration

internal fun KSDeclaration.moduleClassName(): String {
  return "${simpleName.asString()}Module"
}