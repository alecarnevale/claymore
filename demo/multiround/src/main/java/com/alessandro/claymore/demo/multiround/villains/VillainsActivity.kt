package com.alessandro.claymore.demo.multiround.villains

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
import com.alessandro.claymore.demo.multiround.models.Villain
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Qualifier

@AutoProvides(activityClass = VillainsActivity::class)
interface VillainsActivityIntent {
  @Qualifier
  annotation class FirstVillain

  @Qualifier
  annotation class SecondVillain

  operator fun invoke(
    @FirstVillain firstVillain: Villain,
    @SecondVillain secondVillain: Villain,
  ): Intent
}

@AndroidEntryPoint
class VillainsActivity : AppCompatActivity() {

  private val viewModel: VillainsViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val viewState by viewModel.viewState.collectAsState()
      val releaseVillains = remember(viewModel) {
        {
          viewModel.releaseVillains()
        }
      }
      Content(
        firstVillain = viewState.firstVillain,
        secondVillain = viewState.secondVillain,
        releaseVillainsButtonEnables = viewState.releaseVillainsButtonEnabled,
        onReleaseVillainsButtonClicked = { releaseVillains() }
      )
    }
  }
}

@Composable
private fun Content(
  firstVillain: String?,
  secondVillain: String?,
  releaseVillainsButtonEnables: Boolean,
  onReleaseVillainsButtonClicked: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    firstVillain?.let {
      Text("$it appears!")
    } ?: run {
      Text("Waiting for the first villain...")
    }

    Divider(Modifier.padding(vertical = 32.dp))
    secondVillain?.let {
      Text("$it appears!")
    } ?: run {
      Text("Waiting for the second villain...")
    }

    Divider(Modifier.padding(vertical = 32.dp))
    Button(
      enabled = releaseVillainsButtonEnables,
      onClick = { onReleaseVillainsButtonClicked() }) {
      Text("Release villains")
    }
  }
}

@Preview
@Composable
private fun PreviewWithVillains() {
  Content(
    firstVillain = "Shredder",
    secondVillain = "Karai",
    releaseVillainsButtonEnables = false,
    onReleaseVillainsButtonClicked = {}
  )
}

@Preview
@Composable
private fun PreviewWithoutVillains() {
  Content(
    firstVillain = null,
    secondVillain = null,
    releaseVillainsButtonEnables = true,
    onReleaseVillainsButtonClicked = {}
  )
}