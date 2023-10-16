package com.alessandro.claymore.demo.di

// Not used, @AutoBinds annotation will generate this automatically ;)
// after a successful build take a look at :demo/build/generated/ksp/debug/kotlin/com/alessandro/claymore/demo/modelproviders

/*
import com.alessandro.claymore.demo.modelproviders.BazProvider
import com.alessandro.claymore.demo.modelproviders.BazProviderImpl1
import com.alessandro.claymore.demo.modelproviders.BazProviderImpl2
import com.alessandro.claymore.demo.modelproviders.BazProviderImpl3
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface BazProviderModule {
  @Binds
  @IntoSet
  fun bazProvider1(impl: BazProviderImpl1): BazProvider

  @Binds
  @IntoSet
  fun bazProvider2(impl: BazProviderImpl2): BazProvider

  @Binds
  @IntoSet
  fun bazProvider3(impl: BazProviderImpl3): BazProvider
}
*/