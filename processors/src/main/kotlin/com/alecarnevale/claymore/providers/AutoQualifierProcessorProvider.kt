package com.alecarnevale.claymore.providers

import com.alecarnevale.claymore.processors.AutoQualifierProcessor
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

internal class AutoQualifierProcessorProvider : SymbolProcessorProvider {
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    return AutoQualifierProcessor(logger = environment.logger, codeGenerator = environment.codeGenerator)
  }
}
