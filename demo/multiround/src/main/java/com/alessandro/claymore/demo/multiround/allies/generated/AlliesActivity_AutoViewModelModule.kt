package com.alessandro.claymore.demo.multiround.allies.generated

import androidx.lifecycle.SavedStateHandle
import com.alessandro.claymore.demo.multiround.AutoProvidesKeysProvider
import com.alessandro.claymore.demo.multiround.allies.AlliesActivityIntent
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/*
    Input:
      - AlliesActivity::class
      - AlliesActivity_AutoQualifier  <-- non conosciuto al primo round
      - @FirstAlly firstAlly
      - @SecondAlly secondAlly
 */

@Module
@InstallIn(ViewModelComponent::class)
class AlliesActivity_AutoViewModelModule {
  @Provides
  @AlliesActivityIntent.FirstAlly
  fun firstAlly(
    handle: SavedStateHandle,
    @AlliesActivity_AutoQualifier autoProvidesKeysProvider: AutoProvidesKeysProvider
  ): String {
    return requireNotNull(handle[autoProvidesKeysProvider[AlliesActivityIntent.FirstAlly::class]])
  }

  @Provides
  @AlliesActivityIntent.SecondAlly
  fun secondAlly(
    handle: SavedStateHandle,
    @AlliesActivity_AutoQualifier autoProvidesKeysProvider: AutoProvidesKeysProvider
  ): String {
    return requireNotNull(handle[autoProvidesKeysProvider[AlliesActivityIntent.SecondAlly::class]])
  }
}