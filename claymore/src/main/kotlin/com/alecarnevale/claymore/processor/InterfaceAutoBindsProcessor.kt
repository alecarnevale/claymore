package com.alecarnevale.claymore.processor

import com.alecarnevale.claymore.annotation.InterfaceAutoBinds
import com.alecarnevale.claymore.validator.InterfaceAutoBindsValidator
import com.alecarnevale.claymore.visitor.InterfaceAutoBindsVisitor
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.validate

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

    var unresolvedSymbols: List<KSAnnotated> = emptyList()
    val annotationName = InterfaceAutoBinds::class.qualifiedName

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