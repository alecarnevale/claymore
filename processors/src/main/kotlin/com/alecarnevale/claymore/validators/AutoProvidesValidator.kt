@file:OptIn(ExperimentalAnnotation::class)

package com.alecarnevale.claymore.validators

import com.alecarnevale.claymore.annotations.AutoProvides
import com.alecarnevale.claymore.annotations.ExperimentalAnnotation
import com.alecarnevale.claymore.annotations.keyprovider.KeyProviderQualifier
import com.alecarnevale.claymore.utils.extractParameter
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration

internal class AutoProvidesValidator(
  private val resolver: Resolver,
  private val logger: KSPLogger
) {

  /**
   * Check that [symbol] is an interface with an invoke function.
   * @return true if for the activity argument of [symbol], there is an annotation with the same activity between [keyProvidersAnnotated] sequence.
   */
  fun keyProviderAnnotation(
    symbol: KSAnnotated,
    keyProvidersAnnotated: Sequence<KSAnnotated>
  ): KSAnnotated? {
    // check annotated symbol is an interface with an invoke function
    val classDeclaration = symbol.toClassDeclaration() ?: return null
    if (!classDeclaration.isAnInterface()) return null
    classDeclaration.hasInvokeFunction() ?: return null

    // get the activity provided as argument of AutoProvides
    // and search for a KeyProviderQualifier with the same activity
    val activity =
      symbol.extractParameter(
        annotationName = AutoProvides::class.simpleName,
        parameterName = AutoProvides::activityClass.name,
        resolver = resolver,
        logger = logger
      )

    val keyProviderAnnotation = keyProvidersAnnotated.singleOrNull {
      it.extractParameter(
        KeyProviderQualifier::class.simpleName,
        KeyProviderQualifier::activityClass.name,
          resolver = resolver,
        logger = logger
      ) == activity
    }

    if (keyProviderAnnotation == null) {
      logger.warn("$TAG KeyProviderQualifier not found for $activity")
    } else {
      logger.info("$TAG $keyProviderAnnotation found for $activity")
    }

    return keyProviderAnnotation
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