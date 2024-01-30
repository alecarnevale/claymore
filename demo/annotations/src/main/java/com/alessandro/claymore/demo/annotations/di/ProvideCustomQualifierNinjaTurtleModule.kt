package com.alessandro.claymore.demo.annotations.di

/*
// Not used, @AutoBinds annotation will generate this automatically ;)
// after a successful build take a look at :demo/build/generated/ksp/debug/kotlin/com/alessandro/claymore/demo/annotations/services/impl

import com.alessandro.claymore.demo.annotations.CustomQualifierActivity
import com.alessandro.claymore.demo.annotations.services.ProvideWeapon
import com.alessandro.claymore.demo.annotations.services.impl.ProvideDonatelloWeapon
import com.alessandro.claymore.demo.annotations.services.impl.ProvideLeonardoWeapon
import com.alessandro.claymore.demo.annotations.services.impl.ProvideMichelangeloWeapon
import com.alessandro.claymore.demo.annotations.services.impl.ProvideRaffaelloWeapon
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
internal interface ProvideCustomQualifierNinjaTurtleModule {

  @Binds
  @CustomQualifierActivity.LeonardoWeaponQualifier
  fun leonardo(impl: ProvideLeonardoWeapon): ProvideWeapon

  @Binds
  @CustomQualifierActivity.RaffaelloWeaponQualifier
  fun raffaello(impl: ProvideRaffaelloWeapon): ProvideWeapon

  @Binds
  @CustomQualifierActivity.DonatelloWeaponQualifier
  fun donatello(impl: ProvideDonatelloWeapon): ProvideWeapon

  @Binds
  @CustomQualifierActivity.MichelangeloWeaponQualifier
  fun michelangelo(impl: ProvideMichelangeloWeapon): ProvideWeapon
}
*/