package com.alessandro.claymore.demo.autouninstall

import com.alessandro.claymore.demo.autouninstall.models.OtherService

internal class FakeOtherService : OtherService {
  override fun getValue(): String {
    return "FakeOtherService"
  }
}