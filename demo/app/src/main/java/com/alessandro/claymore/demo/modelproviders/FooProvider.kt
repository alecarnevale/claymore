package com.alessandro.claymore.demo.modelproviders

import com.alecarnevale.claymore.annotations.InterfaceAutoBinds
import com.alessandro.claymore.demo.models.Foo
import dagger.hilt.android.components.ActivityComponent

//@InterfaceAutoBinds(implementation = FooProviderImpl::class, component = ActivityComponent::class)
interface FooProvider {
  fun get(): Foo
}