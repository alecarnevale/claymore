package com.alecarnevale.claymore.validators

import com.alecarnevale.claymore.annotations.AutoUninstall
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

/**
 * This validator check if annotated symbol is a class.
 */
internal class AutoUninstallValidator(private val logger: KSPLogger) {
  fun isValid(symbol: KSAnnotated): Boolean {
    return symbol.toClassDeclaration() != null
  }

  private fun KSAnnotated.toClassDeclaration(): KSClassDeclaration? {
    val classDeclaration = (this as? KSClassDeclaration)
    if (classDeclaration == null) {
      logger.error("$TAG ${AutoUninstall::class.simpleName} annotation must annotates class")
    }
    return classDeclaration
  }
}

private const val TAG = "Claymore - AutoUninstallValidator:"