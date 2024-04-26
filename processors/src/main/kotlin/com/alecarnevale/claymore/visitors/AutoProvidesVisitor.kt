@file:OptIn(ExperimentalAnnotation::class)

package com.alecarnevale.claymore.visitors

import com.alecarnevale.claymore.annotations.AutoProvides
import com.alecarnevale.claymore.annotations.ExperimentalAnnotation
import com.alecarnevale.claymore.annotations.keyprovider.KeyProviderQualifier
import com.alecarnevale.claymore.utils.extractParameter
import com.alecarnevale.claymore.writers.AutoIntentImplWriter
import com.alecarnevale.claymore.writers.AutoProvidesKeysProviderWriter
import com.alecarnevale.claymore.writers.AutoViewModelModuleWriter
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSNode
import com.google.devtools.ksp.visitor.KSDefaultVisitor
import com.squareup.kotlinpoet.ksp.writeTo
import kotlin.reflect.KClass

/**
 *
 */
internal class AutoProvidesVisitor(
  val codeGenerator: CodeGenerator,
  val resolver: Resolver,
  val logger: KSPLogger
) : KSDefaultVisitor<KSAnnotated, Unit>() {

  val kclass: KClass<*> = AutoProvides::class

  private val autoProvidesKeysProviderWriter = AutoProvidesKeysProviderWriter()
  private val autoIntentImplWriter = AutoIntentImplWriter()
  private val autoViewModelModuleWriter = AutoViewModelModuleWriter()

  override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: KSAnnotated) {
    logger.info("$TAG visitClassDeclaration of $classDeclaration with data $data")

    val invokeFunctionParams = classDeclaration
      .getAllFunctions()
      .singleOrNull { it.simpleName.getShortName() == "invoke" }
      ?.parameters ?: emptyList()

    autoProvidesKeysProviderWriter.write(
      activityIntentDeclaration = classDeclaration,
      autoQualifierDeclaration = data as KSClassDeclaration,
      parameters = invokeFunctionParams
    ).writeTo(
      codeGenerator = codeGenerator,
      aggregating = false,
      originatingKSFiles = listOf(classDeclaration.containingFile!!)
    )

    val activityDeclaration = requireNotNull(
      data.extractParameter(
        KeyProviderQualifier::class.simpleName,
        KeyProviderQualifier::activityClass.name,
        resolver = resolver,
        logger = logger
      )
    )

    autoIntentImplWriter.write(
      activityIntentDeclaration = classDeclaration,
      activityDeclaration = activityDeclaration,
      autoQualifierDeclaration = data,
      parameters = invokeFunctionParams
    ).writeTo(
      codeGenerator = codeGenerator,
      aggregating = false,
      originatingKSFiles = listOf(classDeclaration.containingFile!!)
    )

    autoViewModelModuleWriter.write(
      activityIntentDeclaration = classDeclaration,
      autoQualifierDeclaration = data,
      parameters = invokeFunctionParams
    ).writeTo(
      codeGenerator = codeGenerator,
      aggregating = false,
      originatingKSFiles = listOf(classDeclaration.containingFile!!)
    )
  }

  override fun defaultHandler(node: KSNode, data: KSAnnotated) = Unit
}

private const val TAG = "Claymore - AutoProvidesVisitor:"