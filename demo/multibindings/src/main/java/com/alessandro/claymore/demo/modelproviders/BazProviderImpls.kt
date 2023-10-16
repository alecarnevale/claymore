package com.alessandro.claymore.demo.modelproviders

import com.alecarnevale.claymore.annotations.AutoBinds
import com.alessandro.claymore.demo.models.Baz
import com.alessandro.claymore.demo.models.BazImpl
import javax.inject.Inject

@AutoBinds(intoSet = true)
class BazProviderImpl1 @Inject constructor() : BazProvider {
  override fun get(): Baz {
    return BazImpl("BazProviderImpl1")
  }
}

@AutoBinds(intoSet = true)
class BazProviderImpl2 @Inject constructor() : BazProvider {
  override fun get(): Baz {
    return BazImpl("BazProviderImpl2")
  }
}

@AutoBinds(intoSet = true)
class BazProviderImpl3 @Inject constructor() : BazProvider {
  override fun get(): Baz {
    return BazImpl("BazProviderImpl3")
  }
}