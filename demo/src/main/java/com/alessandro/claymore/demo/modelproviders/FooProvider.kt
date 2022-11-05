package com.alessandro.claymore.demo.modelproviders

import com.alecarnevale.claymore.annotation.Autobind
import com.alessandro.claymore.demo.models.Foo

@Autobind(implementationClass = FooProviderImpl::class)
interface FooProvider {
  fun get(): Foo
}