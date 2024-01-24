package com.alecarnevale.claymore.visitors

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.symbol.KSVisitorVoid
import kotlin.reflect.KClass

abstract class Visitor : KSVisitorVoid() {
  protected abstract val codeGenerator: CodeGenerator
  protected abstract val resolver: Resolver
  protected abstract val logger: KSPLogger

  protected abstract val kclass: KClass<*>

  private val tag by lazy { "Claymore - Visitor($kclass):" }

  protected fun KSClassDeclaration.extractParameter(parameterName: String): KSClassDeclaration? {
    // extract the KSType
    val annotation = annotations.firstOrNull { it.shortName.getShortName() == kclass.simpleName }
    val parameterKsType =
      annotation?.arguments?.firstOrNull { it.name?.getShortName() == parameterName }?.value as? KSType
    if (parameterKsType == null) {
      logger.error("$tag parameter class not found for $parameterName")
      return null
    }

    // extract the KSName
    val parameterQualifiedName = parameterKsType.declaration.qualifiedName
    if (parameterQualifiedName == null) {
      logger.error("$tag qualified name is null for $parameterName")
      return null
    }

    // extract the KSClassDeclaration
    val parameterDeclaration = resolver.getClassDeclarationByName(parameterQualifiedName)
    if (parameterDeclaration == null) {
      logger.error("$tag implementation class not found for $parameterName")
      return null
    }

    return parameterDeclaration
  }

  protected fun KSClassDeclaration.extractParameters(parameterName: String): List<KSClassDeclaration>? {
    // extract all the KSTypes
    val annotation = annotations.firstOrNull { it.shortName.getShortName() == kclass.simpleName }

    @Suppress("UNCHECKED_CAST")
    val parameterKsTypes =
      annotation?.arguments?.firstOrNull { it.name?.getShortName() == parameterName }?.value as? List<KSType>
    if (parameterKsTypes == null) {
      logger.error("$tag parameter class not found for $parameterName")
      return null
    }

    // resolve all the KSTypes
    val implementationsProvided: List<KSClassDeclaration> =
      parameterKsTypes.mapNotNull { parameterKsType ->
        parameterKsType.declaration.qualifiedName?.let { parameterKsName ->
          resolver.getClassDeclarationByName(parameterKsName)
        }
      }

    return implementationsProvided
  }
}