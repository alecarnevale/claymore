package com.alecarnevale.claymore.annotation

/**
 * This annotation marks the implementation of an interface for which the hilt module will be generated.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AutoBinds(
  // TODO add a parameter to accept custom component
  // val component: KClass<*> = SingletonComponent
)