@file:OptIn(ExperimentalAnnotation::class)

package com.alecarnevale.claymore.processors

import com.alecarnevale.claymore.annotations.AutoProvides
import com.alecarnevale.claymore.annotations.ExperimentalAnnotation
import com.alecarnevale.claymore.annotations.keyprovider.KeyProviderQualifier
import com.alecarnevale.claymore.utils.QualifierWriter
import com.alecarnevale.claymore.utils.extractParameter
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.squareup.kotlinpoet.ksp.writeTo

/**
 * Find and process any symbol annotated with [AutoProvides] to generate a new qualifier annotated with [KeyProviderQualifier].
 */
internal class KeyProviderProcessor(
  private val logger: KSPLogger,
  private val codeGenerator: CodeGenerator
) : SymbolProcessor {

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val autoProvidesAnnotation = AutoProvides::class.qualifiedName ?: return emptyList()

    val autoProvidesAnnotated = resolver.getSymbolsWithAnnotation(autoProvidesAnnotation)
    autoProvidesAnnotated.forEach {
      it.extractParameter(
        annotationName = AutoProvides::class.simpleName,
        parameterName = AutoProvides::activityClass.name,
        resolver = resolver,
        logger = logger
      )
        ?.let { classDeclaration ->
          val writer = QualifierWriter(classDeclaration)
          writer.write().writeTo(
            codeGenerator = codeGenerator,
            aggregating = false,
            originatingKSFiles = listOf(classDeclaration.containingFile!!)
          )
          logger.info("New _AutoQualifier generated for $classDeclaration")
        }
    }

    return emptyList()
  }
}