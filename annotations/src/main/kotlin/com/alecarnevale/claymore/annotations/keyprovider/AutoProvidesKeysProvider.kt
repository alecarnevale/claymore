package com.alecarnevale.claymore.annotations.keyprovider

import com.alecarnevale.claymore.annotations.AutoProvides
import kotlin.reflect.KClass

/**
 * Provides the key used for the annotation when need to be serialized/deserialized in a bundle.
 * For claymore processor use only: this is not intended to be used externally.
 *
 * Using indirectly this provider, through [AutoProvides] annotation, forces client
 * to replace `compileOnly` gradle to `implementation` because
 * AutoProvidesKeysProvider must be in the classpath at runtime.
 */
interface AutoProvidesKeysProvider {
  operator fun get(annotation: KClass<out Annotation>): String
}