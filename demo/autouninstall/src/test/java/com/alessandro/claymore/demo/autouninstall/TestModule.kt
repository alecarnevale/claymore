package com.alessandro.claymore.demo.autouninstall

import com.alessandro.claymore.demo.autouninstall.models.OtherService
import com.alessandro.claymore.demo.autouninstall.models.OtherServiceImplModule
import com.alessandro.claymore.demo.autouninstall.models.Service
import com.alessandro.claymore.demo.autouninstall.models.ServiceImplModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
  components = [SingletonComponent::class],
  replaces = [ServiceImplModule::class, OtherServiceImplModule::class]
)
internal object TestModule {

  @get:Provides
  var fakeService: Service = FakeService()

  @get:Provides
  var fakeOtherService: OtherService = FakeOtherService()
}