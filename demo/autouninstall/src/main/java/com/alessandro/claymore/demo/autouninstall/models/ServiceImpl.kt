package com.alessandro.claymore.demo.autouninstall.models

import com.alecarnevale.claymore.annotations.AutoBinds
import javax.inject.Inject

@AutoBinds
internal class ServiceImpl @Inject constructor(): Service {
  override fun getValue(): String {
    return "ServiceImpl"
  }
}