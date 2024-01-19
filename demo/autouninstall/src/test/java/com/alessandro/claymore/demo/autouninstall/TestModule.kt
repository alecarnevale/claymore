package com.alessandro.claymore.demo.autouninstall

import com.alecarnevale.claymore.annotations.AutoUninstall
import com.alessandro.claymore.demo.autouninstall.models.OtherService
import com.alessandro.claymore.demo.autouninstall.models.OtherServiceImpl
import com.alessandro.claymore.demo.autouninstall.models.Service
import com.alessandro.claymore.demo.autouninstall.models.ServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
@AutoUninstall(implementations = [ServiceImpl::class, OtherServiceImpl::class])
internal object TestModule {

  @get:Provides
  var fakeService: Service = FakeService()

  @get:Provides
  var fakeOtherService: OtherService = FakeOtherService()
}