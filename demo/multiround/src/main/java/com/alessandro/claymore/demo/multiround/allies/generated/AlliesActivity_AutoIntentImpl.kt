package com.alessandro.claymore.demo.multiround.allies.generated

import android.content.Context
import android.content.Intent
import com.alecarnevale.claymore.annotations.AutoBinds
import com.alessandro.claymore.demo.multiround.AutoProvidesKeysProvider
import com.alessandro.claymore.demo.multiround.allies.AlliesActivity
import com.alessandro.claymore.demo.multiround.allies.AlliesActivityIntent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/*
    Input:
      - AlliesActivity::class
      - AlliesActivity_AutoQualifier  <-- non conosciuto al primo round
      - @FirstAlly firstAlly
      - @SecondAlly secondAlly
 */

@AutoBinds
internal class AlliesActivity_AutoIntentImpl @Inject constructor(
  @AlliesActivity_AutoQualifier private val autoProvidesKeysProvider: AutoProvidesKeysProvider,
  @ApplicationContext private val context: Context,
) : AlliesActivityIntent {
  override fun invoke(firstAlly: String, secondAlly: String): Intent {
    return Intent(context, AlliesActivity::class.java)
      .putExtra(autoProvidesKeysProvider[AlliesActivityIntent.FirstAlly::class], firstAlly)
      .putExtra(autoProvidesKeysProvider[AlliesActivityIntent.SecondAlly::class], secondAlly)
  }
}