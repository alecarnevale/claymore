package com.alecarnevale.claymore.validators

import com.alecarnevale.claymore.annotations.AutoBinds
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier
import com.google.devtools.ksp.validate

/**
 * This validator check if annotated symbols is a class.
 */
internal class AutoBindsValidator(private val logger: KSPLogger) {
  fun isValid(symbol: KSAnnotated): Boolean {
    return symbol.validate() && symbol.isAClass()
  }

  private fun KSAnnotated.isAClass(): Boolean {
    if (this !is KSClassDeclaration || this.classKind != ClassKind.CLASS) {
      logger.error("$TAG ${AutoBinds::class.simpleName} annotation must annotates class")
      return false
    }
    if (this.modifiers.contains(Modifier.ABSTRACT)) {
      logger.error("$TAG ${AutoBinds::class.simpleName} annotation must not annotates abstract class")
      return false
    }

    return true
  }
}

private const val TAG = "Claymore - AutoBindsValidator:"