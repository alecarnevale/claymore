package com.alecarnevale.claymore.processor

import com.alecarnevale.claymore.annotation.Autobind
import com.alecarnevale.claymore.validator.AutobindValidator
import com.alecarnevale.claymore.visitor.AutobindVisitor
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.validate

/**
 * Find and process any symbol annotated with [Autobind].
 */
internal class AutobindProcessor(
  private val logger: KSPLogger,
  private val codeGenerator: CodeGenerator
) : SymbolProcessor {

  private val validator = AutobindValidator(logger)

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val visitor = AutobindVisitor(codeGenerator, resolver, logger)

    var unresolvedSymbols: List<KSAnnotated> = emptyList()
    val annotationName = Autobind::class.qualifiedName

    if (annotationName != null) {
      val resolved = resolver
        .getSymbolsWithAnnotation(annotationName)
        .toList()
      val validatedSymbols = resolved.filter { it.validate() }.toList()
      validatedSymbols
        .filter {
          validator.isValid(it)
        }
        .forEach {
          it.accept(visitor, Unit)
        }
      unresolvedSymbols = resolved - validatedSymbols
    }
    return unresolvedSymbols
  }
}