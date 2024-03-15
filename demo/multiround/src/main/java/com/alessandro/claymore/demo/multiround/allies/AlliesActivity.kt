package com.alessandro.claymore.demo.multiround.allies

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alessandro.claymore.demo.multiround.AutoProvides
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Qualifier

@AutoProvides(activityClass = AlliesActivity::class)
interface AlliesActivityIntent {
  @Qualifier
  annotation class FirstAlly

  @Qualifier
  annotation class SecondAlly

  operator fun invoke(
    @FirstAlly firstAlly: String,
    @SecondAlly secondAlly: String,
  ): Intent
}

@AndroidEntryPoint
class AlliesActivity : AppCompatActivity() {

  private val viewModel: AlliesViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val viewState by viewModel.viewState.collectAsState()
      val releaseAllies = remember(viewModel) {
        {
          viewModel.releaseAllies()
        }
      }
      Content(
        firstAlly = viewState.firstAlly,
        secondAlly = viewState.secondAlly,
        releaseAlliesButtonEnables = viewState.releaseAlliesButtonEnabled,
        onReleaseAlliesButtonClicked = { releaseAllies() }
      )
    }
  }
}

@Composable
private fun Content(
  firstAlly: String?,
  secondAlly: String?,
  releaseAlliesButtonEnables: Boolean,
  onReleaseAlliesButtonClicked: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    firstAlly?.let {
      Text("$it appears!")
    } ?: run {
      Text("Waiting for the first ally...")
    }

    Divider(Modifier.padding(vertical = 32.dp))
    secondAlly?.let {
      Text("$it appears!")
    } ?: run {
      Text("Waiting for the second ally...")
    }

    Divider(Modifier.padding(vertical = 32.dp))
    Button(
      enabled = releaseAlliesButtonEnables,
      onClick = { onReleaseAlliesButtonClicked() }) {
      Text("Release allies")
    }
  }
}

@Preview
@Composable
private fun PreviewWithAllies() {
  Content(
    firstAlly = "Splinter",
    secondAlly = "April",
    releaseAlliesButtonEnables = false,
    onReleaseAlliesButtonClicked = {}
  )
}

@Preview
@Composable
private fun PreviewWithoutAllies() {
  Content(
    firstAlly = null,
    secondAlly = null,
    releaseAlliesButtonEnables = true,
    onReleaseAlliesButtonClicked = {}
  )
}