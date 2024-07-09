@file:OptIn(ExperimentalAnnotation::class)

package com.alecarnevale.claymore.validators

import com.alecarnevale.claymore.annotations.AutoProvides
import com.alecarnevale.claymore.annotations.ExperimentalAnnotation
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

/**
 * Check that the annotated symbol is an interface with an invoke function.
 */
internal class AutoProvidesValidator(
  private val resolver: Resolver,
  private val logger: KSPLogger
) {

  fun isValid(symbol: KSAnnotated): Boolean {
    // check annotated symbol is an interface with an invoke function
    val classDeclaration = symbol.toClassDeclaration() ?: return false
    if (!classDeclaration.isAnInterface()) return false
    classDeclaration.hasInvokeFunction() ?: return false

    return true
  }

  private fun KSAnnotated.toClassDeclaration(): KSClassDeclaration? {
    val classDeclaration = (this as? KSClassDeclaration)
    if (classDeclaration == null) {
      logger.error("$TAG ${AutoProvides::class.simpleName} annotation must annotates an interface")
    }
    return classDeclaration
  }

  private fun KSClassDeclaration.isAnInterface(): Boolean {
    if (this.classKind != ClassKind.INTERFACE) {
      logger.error("$TAG ${AutoProvides::class.simpleName} annotation must annotates an interface")
      return false
    }

    return true
  }

  private fun KSClassDeclaration.hasInvokeFunction(): KSFunctionDeclaration? {
    val function = this.getAllFunctions().singleOrNull { it.simpleName.getShortName() == "invoke" }
    if (function == null) {
      logger.error("$TAG ${AutoProvides::class.simpleName} annotation must have a single function")
    }
    return function
  }
}

private const val TAG = "Claymore - AutoProvidesValidator:"