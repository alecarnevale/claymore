package com.alessandro.claymore.demo.modelproviders

import com.alecarnevale.claymore.annotations.AutoBinds
import com.alessandro.claymore.demo.models.Baz
import com.alessandro.claymore.demo.models.BazImpl
import dagger.multibindings.IntoSet
import javax.inject.Inject

@AutoBinds(annotations = [IntoSet::class])
class BazProviderImpl1 @Inject constructor() : BazProvider {
  override fun get(): Baz {
    return BazImpl("BazProviderImpl1")
  }
}

@AutoBinds(annotations = [IntoSet::class])
class BazProviderImpl2 @Inject constructor() : BazProvider {
  override fun get(): Baz {
    return BazImpl("BazProviderImpl2")
  }
}

@AutoBinds(annotations = [IntoSet::class])
class BazProviderImpl3 @Inject constructor() : BazProvider {
  override fun get(): Baz {
    return BazImpl("BazProviderImpl3")
  }
}