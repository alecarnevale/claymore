package com.alecarnevale.claymore.annotations

import kotlin.reflect.KClass

/**
 * This annotation must be used on an interface which contains the operator function "invoke" that accept
 * the input of the activity and returns an Intent.
 * @param activityClass the activity for which the invoke function will be used to retrieve the intent to start it.
 */
annotation class AutoProvides(
  val activityClass: KClass<*>
)