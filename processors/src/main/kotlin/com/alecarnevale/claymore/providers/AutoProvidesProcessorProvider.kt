package com.alecarnevale.claymore.providers

import com.alecarnevale.claymore.processors.AutoProvidesProcessor
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

internal class AutoProvidesProcessorProvider : SymbolProcessorProvider {
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    return AutoProvidesProcessor(logger = environment.logger, codeGenerator = environment.codeGenerator)
  }
}
