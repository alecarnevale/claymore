package com.alessandro.claymore.demo.annotations.services

internal fun interface  ProvideWeapon {
  operator fun invoke(): String
}