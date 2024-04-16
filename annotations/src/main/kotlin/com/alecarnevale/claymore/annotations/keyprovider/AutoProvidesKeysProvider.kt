package com.alecarnevale.claymore.annotations.keyprovider

import kotlin.reflect.KClass

/**
 * Provides the key used for the annotation when need to be serialized/deserialized in a bundle.
 * For claymore processor use only: this is not intended to be used externally.
 */
interface AutoProvidesKeysProvider {
  operator fun get(annotation: KClass<out Annotation>): String
}