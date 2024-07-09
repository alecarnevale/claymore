package com.alecarnevale.claymore.annotations

import kotlin.reflect.KClass

/**
 * This annotation must be used on an interface which contains the operator function "invoke" that accept
 * the input of the activity and returns an Intent.
 *
 * In order to AutoProvides to work properly, add annotations module as `implementation` gradle dependency.
 *
 * @param activityClass the activity for which the invoke function will be used to retrieve the intent to start it.
 */
@ExperimentalAnnotation
annotation class AutoProvides(
  val activityClass: KClass<*>
)