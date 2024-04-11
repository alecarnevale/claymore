package com.alecarnevale.claymore.utils

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSType
import com.google.devtools.ksp.processing.KSPLogger

internal fun KSAnnotated.extractParameter(
  annotationName: String?,
  parameterName: String?,
  resolver: Resolver,
  logger: KSPLogger
): KSClassDeclaration? {
  val annotation =
    annotations.firstOrNull { it.shortName.getShortName() == annotationName }

  val parameterKsType =
    annotation?.arguments?.firstOrNull { it.name?.getShortName() == parameterName }?.value as? KSType

  return parameterKsType?.declaration?.qualifiedName?.let {
    resolver.getClassDeclarationByName(it)
  } ?: run {
    logger.error("KSAnnotated.extractParameter $parameterName class not found for $annotationName")
    null
  }
}