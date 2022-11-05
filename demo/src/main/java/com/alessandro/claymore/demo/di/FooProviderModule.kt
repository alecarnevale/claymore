package com.alessandro.claymore.demo.di

import com.alessandro.claymore.demo.modelproviders.FooProvider
import com.alessandro.claymore.demo.modelproviders.FooProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FooProviderModule {
  @Binds
  fun fooProvider(impl: FooProviderImpl): FooProvider
}