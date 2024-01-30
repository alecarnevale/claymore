package com.alessandro.claymore.demo.annotations.services

import com.alessandro.claymore.demo.annotations.models.NinjaTurtle

internal fun interface ProvideNinjaTurtle {
  operator fun invoke(): NinjaTurtle
}