package com.alecarnevale.claymore.validator

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate

/**
 * This validator check if annotated symbols is an interface.
 */
class AutobindValidator(private val logger: KSPLogger) {
  fun isValid(symbol: KSAnnotated): Boolean {
    return symbol.isAnInterface() && symbol.validate()
  }

  private fun KSAnnotated.isAnInterface(): Boolean {
    if (this !is KSClassDeclaration) {
      logger.error("$TAG @Autobind annotations must annotates interface")
      return false
    }
    if (this.classKind != ClassKind.INTERFACE) {
      logger.error("$TAG @Autobind annotations must annotates interface")
      return false
    }

    return true
  }
}

private const val TAG = "Claymore - AutobindValidator:"