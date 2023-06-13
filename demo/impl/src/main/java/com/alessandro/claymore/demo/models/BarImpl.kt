package com.alessandro.claymore.demo.models

class BarImpl(private val provider: String? = null) : Bar {
  override fun get(): String {
    return "This is BarImpl, provided by: $provider"
  }
}