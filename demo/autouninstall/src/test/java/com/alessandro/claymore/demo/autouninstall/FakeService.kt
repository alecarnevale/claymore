package com.alessandro.claymore.demo.autouninstall

import com.alessandro.claymore.demo.autouninstall.models.Service

internal class FakeService : Service {
  override fun getValue(): String {
    return "FakeService"
  }
}