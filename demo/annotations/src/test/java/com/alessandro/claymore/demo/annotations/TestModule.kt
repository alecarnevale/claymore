package com.alessandro.claymore.demo.annotations

import com.alecarnevale.claymore.annotations.AutoUninstall
import com.alessandro.claymore.demo.annotations.services.ProvideWeapon
import com.alessandro.claymore.demo.annotations.services.impl.ProvideDonatelloWeapon
import com.alessandro.claymore.demo.annotations.services.impl.ProvideLeonardoWeapon
import com.alessandro.claymore.demo.annotations.services.impl.ProvideMichelangeloWeapon
import com.alessandro.claymore.demo.annotations.services.impl.ProvideRaffaelloWeapon
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

// Avoid using TestInstallIn annotation and reference generated module class.
// AutoUninstall annotation will uninstall generated module, using implementation class as reference.
//@TestInstallIn(
//  components = [ActivityComponent::class],
//  replaces = [
//    ProvideLeonardoWeaponModule::class,
//    ProvideRaffaelloWeaponModule::class,
//    ProvideDonatelloWeaponModule::class,
//    ProvideMichelangeloWeaponModule::class,
//  ]
//)
@AutoUninstall(
  components = [ActivityComponent::class],
  implementations = [
    ProvideLeonardoWeapon::class,
    ProvideRaffaelloWeapon::class,
    ProvideDonatelloWeapon::class,
    ProvideMichelangeloWeapon::class,
  ]
)
@InstallIn(ActivityComponent::class)
@Module
internal object TestModule {

  @Provides
  @CustomQualifierActivity.LeonardoWeaponQualifier
  fun fakeKatana(): ProvideWeapon = ProvideWeapon { "a fake wooden katana" }

  @Provides
  @CustomQualifierActivity.RaffaelloWeaponQualifier
  fun fakeSai(): ProvideWeapon = ProvideWeapon { "a fake nerfed sai" }

  @Provides
  @CustomQualifierActivity.DonatelloWeaponQualifier
  fun fakeStaff(): ProvideWeapon = ProvideWeapon { "a fake broken staff" }

  @Provides
  @CustomQualifierActivity.MichelangeloWeaponQualifier
  fun fakeNunchaku(): ProvideWeapon = ProvideWeapon { "a fake rubber nunchaku" }
}