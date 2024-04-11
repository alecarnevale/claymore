@file:OptIn(ExperimentalAnnotation::class)

package com.alecarnevale.claymore.processors

import com.alecarnevale.claymore.annotations.AutoProvides
import com.alecarnevale.claymore.annotations.ExperimentalAnnotation
import com.alecarnevale.claymore.annotations.keyprovider.KeyProviderQualifier
import com.alecarnevale.claymore.writers.AutoQualifierWriter
import com.alecarnevale.claymore.utils.extractParameter
import com.alecarnevale.claymore.validators.AutoQualifierValidator
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * Find and process any symbol annotated with [AutoProvides] to generate a new qualifier annotated with [KeyProviderQualifier].
 */
internal class AutoQualifierProcessor(
  private val logger: KSPLogger,
  private val codeGenerator: CodeGenerator
) : SymbolProcessor {

  private var round = 0
  private val TAG: String
    get()= "Claymore - AutoQualifierProcessor($round):"

  override fun process(resolver: Resolver): List<KSAnnotated> {
    round++
    val validator = AutoQualifierValidator(resolver, logger)

    // retrieve all AutoProvides annotation to understand which _AutoQualifier must be generated
    // return if no AutoProvides found
    val autoProvidesAnnotation = AutoProvides::class.qualifiedName ?: return emptyList()
    val autoProvidesAnnotated = resolver.getSymbolsWithAnnotation(autoProvidesAnnotation)

    // write _AutoQualifier annotation only for those activity that doesn't have _AutoQualifier yet
    // compare with existing KeyProviderQualifier, to understand which must be generated
    val keyProviderAnnotation = KeyProviderQualifier::class.qualifiedName
    val keyProviderAnnotated =
      keyProviderAnnotation?.let { resolver.getSymbolsWithAnnotation(it) } ?: emptySequence()

    autoProvidesAnnotated.forEach {
      it.extractParameter(
        annotationName = AutoProvides::class.simpleName,
        parameterName = AutoProvides::activityClass.name,
        resolver = resolver,
        logger = logger
      )
        ?.let { classDeclaration ->
          val autoQualifierAnnotation = validator.getKeyProviderAnnotation(
            activityClass = classDeclaration,
            keyProvidersAnnotated = keyProviderAnnotated
          )
          if (autoQualifierAnnotation == null) {
            val writer = AutoQualifierWriter(classDeclaration)
            val autoQualifierFileSpec = writer.write()
            autoQualifierFileSpec.writeTo(
              codeGenerator = codeGenerator,
              aggregating = false,
              originatingKSFiles = listOf(classDeclaration.containingFile!!)
            )
            logger.info("$TAG New ${autoQualifierFileSpec.name} generated for $classDeclaration")
          } else {
            logger.info("$TAG ${(autoQualifierAnnotation as KSClassDeclaration).simpleName.asString()} already exists for $classDeclaration")
          }
        }
    }

    return emptyList()
  }
}