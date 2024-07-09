package com.alecarnevale.claymore.annotations.keyprovider

import kotlin.reflect.KClass

/**
 * Annotated the annotation used to qualify the instance of KeyProviderQualifier.
 * For claymore processor use only: this is not intended to be used externally.
 */
annotation class KeyProviderQualifier(
  val activityClass: KClass<*>
)
