package com.alecarnevale.claymore.validators

import com.alecarnevale.claymore.annotations.keyprovider.KeyProviderQualifier
import com.alecarnevale.claymore.utils.extractParameter
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration

internal class AutoQualifierValidator(
  private val resolver: Resolver,
  private val logger: KSPLogger
) {

  /**
   * Retrieves the annotation annotated with [KeyProviderQualifier] with the requested activity class.
   * Return null if it doesn't exists.
   */
  fun getKeyProviderAnnotation(
    activityClass: KSClassDeclaration,
    keyProvidersAnnotated: Sequence<KSAnnotated>
  ): KSAnnotated? {
    val keyProviderAnnotation = keyProvidersAnnotated.singleOrNull {
      it.extractParameter(
        KeyProviderQualifier::class.simpleName,
        KeyProviderQualifier::activityClass.name,
        resolver = resolver,
        logger = logger
      ) == activityClass
    }

    if (keyProviderAnnotation == null) {
      logger.warn("$TAG _AutoQualifier annotation with KeyProviderQualifier not found for $activityClass")
    } else {
      logger.info("$TAG _AutoQualifier annotation $keyProviderAnnotation found for $activityClass")
    }

    return keyProviderAnnotation
  }
}

private const val TAG = "Claymore - AutoQualifierValidator:"