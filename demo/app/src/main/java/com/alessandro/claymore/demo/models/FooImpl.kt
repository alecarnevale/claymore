package com.alessandro.claymore.demo.models

class FooImpl(private val provider: String? = null) : Foo {
  override fun get(): String {
    return "This is FooImpl, provided by: $provider"
  }
}