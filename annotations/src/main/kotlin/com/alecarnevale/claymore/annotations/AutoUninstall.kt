package com.alecarnevale.claymore.annotations

import dagger.hilt.components.SingletonComponent
import kotlin.reflect.KClass

/**
 * This annotation is used to uninstall module generated with [AutoBinds] and [InterfaceAutoBinds].
 *
 * @param implementations classes for which [AutoBinds] or [InterfaceAutoBinds] has been applied,
 * and related module must be uninstalled. Implementation class expected.
 * @param components where install the new test module.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class AutoUninstall(
  val implementations: Array<KClass<*>>,
  val components: Array<KClass<*>> = [SingletonComponent::class],
)
