@file:OptIn(ExperimentalAnnotation::class)

package com.alecarnevale.claymore.visitors

import com.alecarnevale.claymore.annotations.AutoProvides
import com.alecarnevale.claymore.annotations.ExperimentalAnnotation
import com.alecarnevale.claymore.writers.AutoProvidesWriter
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

  private val writer = AutoProvidesWriter()

  override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: KSAnnotated) {
    logger.info("$TAG visitClassDeclaration of $classDeclaration with data $data")

    val invokeFunctionParams = classDeclaration
      .getAllFunctions()
      .singleOrNull { it.simpleName.getShortName() == "invoke" }
      ?.parameters ?: emptyList()

    writer.writeAutoProvidesKeysProvider(
      activityIntentDeclaration = classDeclaration,
      autoQualifierDeclaration = data as KSClassDeclaration,
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