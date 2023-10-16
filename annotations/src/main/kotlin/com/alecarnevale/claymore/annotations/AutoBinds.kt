package com.alecarnevale.claymore.annotations

import dagger.hilt.components.SingletonComponent
import kotlin.reflect.KClass

/**
 * This annotation marks the implementation of an interface for which the hilt module will be generated.
 * @param component in which dagger component must be installed the module. If not specified, [SingletonComponent] will be used as default value.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AutoBinds(
  val component: KClass<*> = SingletonComponent::class,
  val intoSet: Boolean = false,
)