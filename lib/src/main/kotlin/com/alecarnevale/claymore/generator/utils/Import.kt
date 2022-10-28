package com.alecarnevale.claymore.generator.utils

import com.alecarnevale.claymore.Constants
import com.google.devtools.ksp.symbol.KSDeclaration

internal fun StringBuilder.packageName(): StringBuilder {
  return appendLine("package ${Constants.packageName}")
}

internal fun StringBuilder.staticImport(): StringBuilder {
  return appendLine("import dagger.Binds")
    .appendLine("import dagger.Module")
    .appendLine("import dagger.hilt.InstallIn")
    .appendLine("import dagger.hilt.components.SingletonComponent")
}

internal fun StringBuilder.classImport(declaration: KSDeclaration): StringBuilder {
  return appendLine("import ${declaration.qualifiedName?.asString()}")
}