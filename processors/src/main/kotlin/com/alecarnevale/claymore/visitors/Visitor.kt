package com.alecarnevale.claymore.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSVisitorVoid

abstract class Visitor : KSVisitorVoid() {
  protected abstract val codeGenerator: CodeGenerator
  protected abstract val resolver: Resolver
  protected abstract val logger: KSPLogger
}