package com.alecarnevale.claymore.validators

import com.alecarnevale.claymore.annotations.AutoBinds
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.Modifier

/**
 * This validator check if annotated symbols is a class, and if it implements a single known interface.
 */
internal class AutoBindsValidator(private val logger: KSPLogger) {
  fun isValid(symbol: KSAnnotated): Boolean {
    val classDeclaration = symbol.toClassDeclaration() ?: return false
    return classDeclaration.isAClass() && classDeclaration.extendsOrImplementsSomeType()
  }

  private fun KSAnnotated.toClassDeclaration(): KSClassDeclaration? {
    val classDeclaration = (this as? KSClassDeclaration)
    if (classDeclaration == null) {
      logger.error("$TAG ${AutoBinds::class.simpleName} annotation must annotates class")
    }
    return classDeclaration
  }

  private fun KSClassDeclaration.isAClass(): Boolean {
    if (this.classKind != ClassKind.CLASS) {
      logger.error("$TAG ${AutoBinds::class.simpleName} annotation must annotates class")
      return false
    }
    if (this.modifiers.contains(Modifier.ABSTRACT)) {
      logger.error("$TAG ${AutoBinds::class.simpleName} annotation must not annotates abstract class")
      return false
    }

    return true
  }

  private fun KSClassDeclaration.extendsOrImplementsSomeType(): Boolean =
    when (superTypes.count()) {
      0 -> false.also {
        logger.error("$TAG ${AutoBinds::class.simpleName} $this musts extends or implements a super type.")
      }

      1 -> {
        val superType = superTypes.single()
        if (superType.resolve().isError) {
          logger.error("$TAG Cannot resolve interface $superType.")
          false
        } else {
          true
        }
      }

      else -> false.also {
        logger.error("$TAG ${AutoBinds::class.simpleName} $this musts implement at most one interface.")
      }
    }
}

private const val TAG = "Claymore - AutoBindsValidator:"