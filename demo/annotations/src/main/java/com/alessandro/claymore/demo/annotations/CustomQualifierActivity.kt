package com.alessandro.claymore.demo.annotations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alessandro.claymore.demo.annotations.services.ProvideWeapon
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Qualifier

@AndroidEntryPoint
class CustomQualifierActivity : AppCompatActivity() {

  @Qualifier
  annotation class LeonardoWeaponQualifier

  @Qualifier
  annotation class RaffaelloWeaponQualifier

  @Qualifier
  annotation class DonatelloWeaponQualifier

  @Qualifier
  annotation class MichelangeloWeaponQualifier

  @Inject
  @LeonardoWeaponQualifier
  internal lateinit var provideLeonardoWeapon: ProvideWeapon

  @Inject
  @RaffaelloWeaponQualifier
  internal lateinit var provideRaffaelloWeapon: ProvideWeapon

  @Inject
  @DonatelloWeaponQualifier
  internal lateinit var provideDonatelloWeapon: ProvideWeapon

  @Inject
  @MichelangeloWeaponQualifier
  internal lateinit var provideMichelangeloWeapon: ProvideWeapon

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Content(
        leonardoWeapon = provideLeonardoWeapon(),
        raffaelloWeapon = provideRaffaelloWeapon(),
        donatelloWeapon = provideDonatelloWeapon(),
        michelangeloWeapon = provideMichelangeloWeapon()
      )
    }
  }

  companion object {
    fun intent(context: Context) = Intent(context, CustomQualifierActivity::class.java)
  }
}

@Composable
private fun Content(
  leonardoWeapon: String,
  raffaelloWeapon: String,
  donatelloWeapon: String,
  michelangeloWeapon: String,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text("Leonardo wields $leonardoWeapon")
    Spacer(modifier = Modifier.height(20.dp))
    Text("Raffaello wields $raffaelloWeapon")
    Spacer(modifier = Modifier.height(20.dp))
    Text("Donatello wields $donatelloWeapon")
    Spacer(modifier = Modifier.height(20.dp))
    Text("Michelangelo wields $michelangeloWeapon")
  }
}

@Preview
@Composable
private fun Preview() {
  Content(
    leonardoWeapon = "Katana",
    raffaelloWeapon = "Sai",
    donatelloWeapon = "Staff",
    michelangeloWeapon = "Nunchaku",
  )
}