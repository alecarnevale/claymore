package com.alessandro.claymore.demo.di

// Not used, InterfaceAutoBinds annotation will generate this automatically ;)
// after a successful build take a look at :demo/build/generated/ksp/debug/kotlin/com/alessandro/claymore/demo/modelproviders


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
