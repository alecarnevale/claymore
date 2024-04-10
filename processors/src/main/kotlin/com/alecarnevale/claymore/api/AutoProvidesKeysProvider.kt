package com.alecarnevale.claymore.api

import kotlin.reflect.KClass

/**
 * Provides the key used for the annotation when need to be serialized/deserialized in a bundle.
 * For internal use only.
 */
internal interface AutoProvidesKeysProvider {
  operator fun get(annotation: KClass<out Annotation>): String
}