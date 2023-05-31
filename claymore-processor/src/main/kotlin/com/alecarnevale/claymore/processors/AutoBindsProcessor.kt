package com.alecarnevale.claymore.processors

import com.alecarnevale.claymore.annotations.AutoBinds
import com.alecarnevale.claymore.validators.AutoBindsValidator
import com.alecarnevale.claymore.visitors.AutoBindsVisitor
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * Find and process any symbol annotated with [AutoBinds].
 */
internal class AutoBindsProcessor(
  private val logger: KSPLogger,
  private val codeGenerator: CodeGenerator
) : SymbolProcessor {

  private val validator = AutoBindsValidator(logger)

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val visitor = AutoBindsVisitor(codeGenerator, resolver, logger)

    val annotationName = AutoBinds::class.qualifiedName ?: return emptyList()

    val resolvedSymbols = resolver.getSymbolsWithAnnotation(annotationName)
    val validatedSymbols = resolvedSymbols.filter { validator.isValid(it) }.toSet()
    validatedSymbols.forEach {
      it.accept(visitor, Unit)
    }

    return (resolvedSymbols - validatedSymbols).toList()
  }
}