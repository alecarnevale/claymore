package com.alessandro.claymore.demo.multiround.villains.generated

import androidx.lifecycle.SavedStateHandle
import com.alessandro.claymore.demo.multiround.AutoProvidesKeysProvider
import com.alessandro.claymore.demo.multiround.models.Villain
import com.alessandro.claymore.demo.multiround.villains.VillainsActivityIntent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/*
    Input:
      - VillainsActivity::class
      - VillainsActivity_AutoQualifier  <-- non conosciuto al primo round
      - @FirstVillain firstVillain
      - @SecondVillain secondVillain
 */

@Module
@InstallIn(ViewModelComponent::class)
class VillainsActivity_AutoViewModelModule {
  @Provides
  @VillainsActivityIntent.FirstVillain
  fun firstVillain(
    handle: SavedStateHandle,
    @VillainsActivity_AutoQualifier autoProvidesKeysProvider: AutoProvidesKeysProvider
  ): Villain {
    return requireNotNull(handle[autoProvidesKeysProvider[VillainsActivityIntent.FirstVillain::class]])
  }

  @Provides
  @VillainsActivityIntent.SecondVillain
  fun secondVillain(
    handle: SavedStateHandle,
    @VillainsActivity_AutoQualifier autoProvidesKeysProvider: AutoProvidesKeysProvider
  ): Villain {
    return requireNotNull(handle[autoProvidesKeysProvider[VillainsActivityIntent.SecondVillain::class]])
  }
}