package com.alecarnevale.claymore.validators

import com.alecarnevale.claymore.annotations.InterfaceAutoBinds
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier

/**
 * This validator check if annotated symbols is an interface.
 */
internal class InterfaceAutoBindsValidator(private val logger: KSPLogger) {
  fun isValid(symbol: KSAnnotated): Boolean {
    return symbol.isAnInterface()
  }

  private fun KSAnnotated.isAnInterface(): Boolean {
    if (this !is KSClassDeclaration || this.isNotValidSupertype()) {
      logger.error("$TAG ${InterfaceAutoBinds::class.simpleName} annotation must annotates interface")
      return false
    }

    return true
  }

  private fun KSClassDeclaration.isNotValidSupertype(): Boolean = !isValidSupertype()

  private fun KSClassDeclaration.isValidSupertype(): Boolean {
    if (classKind == ClassKind.INTERFACE) {
      return true
    }
    if (classKind == ClassKind.CLASS && this.modifiers.contains(Modifier.ABSTRACT)) {
      return true
    }
    return false
  }
}

private const val TAG = "Claymore - InterfaceAutoBindsValidator:"