package com.alecarnevale.claymore.processors

import com.alecarnevale.claymore.annotations.InterfaceAutoBinds
import com.alecarnevale.claymore.validators.InterfaceAutoBindsValidator
import com.alecarnevale.claymore.visitors.InterfaceAutoBindsVisitor
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * Find and process any symbol annotated with [InterfaceAutoBinds].
 */
internal class InterfaceAutoBindsProcessor(
  private val logger: KSPLogger,
  private val codeGenerator: CodeGenerator
) : SymbolProcessor {

  private val validator = InterfaceAutoBindsValidator(logger)

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val visitor = InterfaceAutoBindsVisitor(codeGenerator, resolver, logger)

    val annotationName = InterfaceAutoBinds::class.qualifiedName ?: return emptyList()

    val resolvedSymbols = resolver.getSymbolsWithAnnotation(annotationName)
    val validatedSymbols = resolvedSymbols.filter { validator.isValid(it) }.toSet()
    validatedSymbols.forEach {
      it.accept(visitor, Unit)
    }

    return (resolvedSymbols - validatedSymbols).toList()
  }
}