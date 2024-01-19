package com.alessandro.claymore.demo.autouninstall.models

import com.alecarnevale.claymore.annotations.AutoBinds
import javax.inject.Inject

@AutoBinds
internal class OtherServiceImpl @Inject constructor() : OtherService {
  override fun getValue(): String {
    return "OtherServiceImpl"
  }
}