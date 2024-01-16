package com.alecarnevale.claymore.providers

import com.alecarnevale.claymore.processors.AutoUninstallProcessor
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

internal class AutoUninstallProcessorProvider : SymbolProcessorProvider {
  override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
    return AutoUninstallProcessor(logger = environment.logger, codeGenerator = environment.codeGenerator)
  }
}
