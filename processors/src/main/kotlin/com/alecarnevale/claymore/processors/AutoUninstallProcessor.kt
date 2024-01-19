package com.alecarnevale.claymore.processors

import com.alecarnevale.claymore.annotations.AutoUninstall
import com.alecarnevale.claymore.validators.AutoUninstallValidator
import com.alecarnevale.claymore.visitors.AutoUninstallVisitor
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

/**
 * Find and process any symbol annotated with [AutoUninstall].
 */
internal class AutoUninstallProcessor(
  private val logger: KSPLogger,
  private val codeGenerator: CodeGenerator
) : SymbolProcessor {

  private val validator = AutoUninstallValidator(logger)

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val visitor = AutoUninstallVisitor(codeGenerator, resolver, logger)

    val annotationName = AutoUninstall::class.qualifiedName ?: return emptyList()

    val resolvedSymbols = resolver.getSymbolsWithAnnotation(annotationName)
    val validatedSymbols = resolvedSymbols.filter { validator.isValid(it) }.toSet()
    validatedSymbols.forEach {
      it.accept(visitor, Unit)
    }

    return (resolvedSymbols - validatedSymbols).toList()
  }
}