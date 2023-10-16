package com.alessandro.claymore.demo.models

class BazImpl(private val provider: String? = null) : Baz {
  override fun get(): String {
    return "This is BazImpl, provided by: $provider"
  }
}