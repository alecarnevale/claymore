package com.alessandro.claymore.demo.annotations.di

/*
// Not used, @AutoBinds annotation will generate this automatically ;)
// after a successful build take a look at :demo/build/generated/ksp/debug/kotlin/com/alessandro/claymore/demo/annotations/services/impl

import com.alessandro.claymore.demo.annotations.services.impl.ProvideDonatello
import com.alessandro.claymore.demo.annotations.services.impl.ProvideLeonardo
import com.alessandro.claymore.demo.annotations.services.impl.ProvideMichelangelo
import com.alessandro.claymore.demo.annotations.services.ProvideNinjaTurtle
import com.alessandro.claymore.demo.annotations.services.impl.ProvideRaffaello
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(ActivityComponent::class)
internal interface ProvideMultiBindingNinjaTurtleModule {
  @Binds
  @IntoSet
  fun leonardo(impl: ProvideLeonardo): ProvideNinjaTurtle

  @Binds
  @IntoSet
  fun raffaello(impl: ProvideRaffaello): ProvideNinjaTurtle

  @Binds
  @IntoSet
  fun donatello(impl: ProvideDonatello): ProvideNinjaTurtle

  @Binds
  @IntoSet
  fun michelangelo(impl: ProvideMichelangelo): ProvideNinjaTurtle
}
*/