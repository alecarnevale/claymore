package com.alecarnevale.claymore.processor

import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

internal class InterfaceAutoBindsProcessorProvider : SymbolProcessorProvider {
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    return InterfaceAutoBindsProcessor(logger = environment.logger, codeGenerator = environment.codeGenerator)
  }
}
