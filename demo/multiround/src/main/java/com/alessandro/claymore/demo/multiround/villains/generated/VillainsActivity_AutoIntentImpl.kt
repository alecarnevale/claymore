package com.alessandro.claymore.demo.multiround.villains.generated

import android.content.Context
import android.content.Intent
import com.alecarnevale.claymore.annotations.AutoBinds
import com.alessandro.claymore.demo.multiround.AutoProvidesKeysProvider
import com.alessandro.claymore.demo.multiround.models.Villain
import com.alessandro.claymore.demo.multiround.villains.VillainsActivity
import com.alessandro.claymore.demo.multiround.villains.VillainsActivityIntent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/*
    Input:
      - VillainsActivity::class
      - VillainsActivity_AutoQualifier  <-- non conosciuto al primo round
      - @FirstVillain firstVillain
      - @SecondVillain secondVillain
 */

@AutoBinds
internal class VillainsActivity_AutoIntentImpl @Inject constructor(
  @VillainsActivity_AutoQualifier private val autoProvidesKeysProvider: AutoProvidesKeysProvider,
  @ApplicationContext private val context: Context,
) : VillainsActivityIntent {
  override fun invoke(firstVillain: Villain, secondVillain: Villain): Intent {
    return Intent(context, VillainsActivity::class.java)
      .putExtra(autoProvidesKeysProvider[VillainsActivityIntent.FirstVillain::class], firstVillain)
      .putExtra(autoProvidesKeysProvider[VillainsActivityIntent.SecondVillain::class], secondVillain)
  }
}