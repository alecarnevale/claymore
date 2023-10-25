package com.alessandro.claymore.demo.autouninstall

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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alessandro.claymore.demo.autouninstall.models.OtherService
import com.alessandro.claymore.demo.autouninstall.models.Service
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AutoUninstallActivity : AppCompatActivity() {

  @Inject
  internal lateinit var service: Service
  private val serviceValue: String by lazy { service.getValue() }

  @Inject
  internal lateinit var otherService: OtherService
  private val otherServiceValue: String by lazy { otherService.getValue() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Content(
        serviceValue = serviceValue,
        otherServiceValue = otherServiceValue,
      )
    }
  }

  companion object {
    fun intent(context: Context) = Intent(context, AutoUninstallActivity::class.java)
  }
}

@Composable
private fun Content(
  serviceValue: String,
  otherServiceValue: String,
) {
  Column(
    modifier = Modifier
      .fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = serviceValue)
    Spacer(modifier = Modifier.height(20.dp))
    Text(text = otherServiceValue)
  }
}

@Preview
@Composable
private fun Preview() {
  Content(
    serviceValue = "Service value",
    otherServiceValue = "Other service value",
  )
}