@file:OptIn(ExperimentalAnnotation::class)

package com.alecarnevale.claymore.processors

import com.alecarnevale.claymore.annotations.AutoProvides
import com.alecarnevale.claymore.annotations.ExperimentalAnnotation
import com.alecarnevale.claymore.annotations.keyprovider.KeyProviderQualifier
import com.alecarnevale.claymore.utils.extractParameter
import com.alecarnevale.claymore.validators.AutoProvidesValidator
import com.alecarnevale.claymore.validators.AutoQualifierValidator
import com.alecarnevale.claymore.visitors.AutoProvidesVisitor
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import kotlin.math.log

/**
 * Find and process any symbol annotated with [AutoProvides].
 */
internal class AutoProvidesProcessor(
  private val logger: KSPLogger,
  private val codeGenerator: CodeGenerator
) : SymbolProcessor {

  private var round = 0
  private val TAG: String
    get()= "Claymore - AutoProvidesProcessor($round):"

  override fun process(resolver: Resolver): List<KSAnnotated> {
    round++
    val visitor = AutoProvidesVisitor(codeGenerator = codeGenerator, resolver = resolver, logger = logger)

    val autoProvidesValidator = AutoProvidesValidator(resolver, logger)
    val autoQualifierValidator = AutoQualifierValidator(resolver, logger)

    val autoProvidesAnnotation = AutoProvides::class.qualifiedName ?: return emptyList()
    val autoProvidesAnnotated = resolver.getSymbolsWithAnnotation(autoProvidesAnnotation)

    val keyProviderAnnotation = KeyProviderQualifier::class.qualifiedName ?: return emptyList()
    val keyProvidersAnnotated = resolver.getSymbolsWithAnnotation(keyProviderAnnotation)

    val deferred = mutableListOf<KSAnnotated>()
    autoProvidesAnnotated.filter { autoProvidesValidator.isValid(it) }.forEach {
      val activityClass = requireNotNull(
        it.extractParameter(
          annotationName = AutoProvides::class.simpleName,
          parameterName = AutoProvides::activityClass.name,
          resolver = resolver,
          logger = logger
        )
      )
      val autoQualifierAnnotation = autoQualifierValidator.getKeyProviderAnnotation(
        activityClass = activityClass,
        keyProvidersAnnotated = keyProvidersAnnotated
      )
      if (autoQualifierAnnotation == null) {
        logger.info("$TAG Deferring $it to the next round")
        deferred.add(it)
      } else {
        logger.info("$TAG Not deferring $it")
        it.accept(visitor, autoQualifierAnnotation)
      }
    }

    return deferred
  }
}