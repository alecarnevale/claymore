package com.alecarnevale.claymore.annotation

import kotlin.reflect.KClass

/**
 * This annotation marks the interface for which the hilt module will be generated.
 * @param implementationClass implementation of the annotated interface that will be provided in the module.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class InterfaceAutoBinds(
  val implementationClass: KClass<*>
  // TODO add a parameter to accept custom component
  // val component: KClass<*> = SingletonComponent
)