package com.alecarnevale.claymore.providers

import com.alecarnevale.claymore.processors.AutoBindsProcessor
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

internal class AutoBindsProcessorProvider : SymbolProcessorProvider {
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    return AutoBindsProcessor(logger = environment.logger, codeGenerator = environment.codeGenerator)
  }
}
