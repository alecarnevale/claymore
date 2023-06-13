package com.alessandro.claymore.demo.modelproviders

import com.alessandro.claymore.demo.models.Foo
import com.alessandro.claymore.demo.models.FooImpl
import javax.inject.Inject

class FooProviderImpl @Inject constructor() : FooProvider {
  override fun get(): Foo {
    return FooImpl("FooProviderImpl")
  }
}