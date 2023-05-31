package com.alessandro.claymore.demo.modelproviders

import com.alecarnevale.claymore.annotations.InterfaceAutoBinds
import com.alessandro.claymore.demo.models.Foo

@InterfaceAutoBinds(implementationClass = FooProviderImpl::class)
interface FooProvider {
  fun get(): Foo
}