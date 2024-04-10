package com.alecarnevale.claymore.providers

import com.alecarnevale.claymore.processors.KeyProviderProcessor
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

internal class KeyProviderProcessorProvider : SymbolProcessorProvider {
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    return KeyProviderProcessor(logger = environment.logger, codeGenerator = environment.codeGenerator)
  }
}
