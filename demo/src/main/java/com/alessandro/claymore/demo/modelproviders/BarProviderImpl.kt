package com.alessandro.claymore.demo.modelproviders

import com.alecarnevale.claymore.annotation.AutoBinds
import com.alessandro.claymore.demo.models.Bar
import com.alessandro.claymore.demo.models.BarImpl
import javax.inject.Inject

@AutoBinds
class BarProviderImpl @Inject constructor() : BarProvider {
  override fun get(): Bar {
    return BarImpl("BarProviderImpl")
  }
}