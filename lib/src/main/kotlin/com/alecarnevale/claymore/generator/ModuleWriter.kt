package com.alecarnevale.claymore.generator

import com.alecarnevale.claymore.generator.utils.*
import com.google.devtools.ksp.symbol.KSDeclaration

// TODO replace with KotlinPoet
/**
 * Write a hilt module that binds [implementationDeclaration] for [interfaceDeclaration].
 */
internal class ModuleWriter(
  private val interfaceDeclaration: KSDeclaration,
  private val implementationDeclaration: KSDeclaration
) {

  fun text() = buildString {
    packageName()
    newLine()

    staticImport()
    classImport(interfaceDeclaration)
    classImport(implementationDeclaration)
    newLine()

    moduleAnnotations()
    interfaceDefinition()
    append("\t")
    bindAnnotation()
    funDefinition()
    appendLine("}")
  }

  private fun StringBuilder.interfaceDefinition(): StringBuilder {
    return appendLine("interface ${interfaceDeclaration.moduleClassName()} {")
  }

  private fun StringBuilder.funDefinition(): StringBuilder {
    return append("\t fun")
      .append(" ")
      .append(interfaceDeclaration.simpleName.asString().replaceFirstChar { it.lowercase() })
      .append("(impl: ${implementationDeclaration.simpleName.asString()}")
      .append("):")
      .append(" ")
      .append(interfaceDeclaration.simpleName.asString())
      .appendLine()
  }
}
