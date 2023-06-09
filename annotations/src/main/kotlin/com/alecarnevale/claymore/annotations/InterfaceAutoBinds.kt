package com.alecarnevale.claymore.annotations

import dagger.hilt.components.SingletonComponent
import kotlin.reflect.KClass

/**
 * This annotation marks the interface for which the hilt module will be generated.
 * @param implementation implementation of the annotated interface that will be provided in the module.
 * @param component in which dagger component must be installed the module. If not specified, [SingletonComponent] will be used as default value.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class InterfaceAutoBinds(
  val implementation: KClass<*>,
  val component: KClass<*> = SingletonComponent::class
)