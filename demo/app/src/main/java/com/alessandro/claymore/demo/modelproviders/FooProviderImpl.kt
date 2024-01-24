package com.alessandro.claymore.demo.modelproviders

import com.alecarnevale.claymore.annotations.AutoBinds
import com.alessandro.claymore.demo.models.Foo
import com.alessandro.claymore.demo.models.FooImpl
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@AutoBinds(component = ActivityComponent::class)
class FooProviderImpl @Inject constructor() : FooProvider {
  override fun get(): Foo {
    return FooImpl("FooProviderImpl")
  }
}