package com.alessandro.claymore.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alessandro.claymore.demo.annotations.CustomQualifierActivity
import com.alessandro.claymore.demo.annotations.MultiBindingActivity
import com.alessandro.claymore.demo.multiround.allies.AlliesActivityIntent
import com.alessandro.claymore.demo.services.DeepThought
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var deepThought: DeepThought
  private val ultimateAnswer: String by lazy { deepThought.getAnswer() }

  @Inject
  lateinit var alliesActivityIntent: AlliesActivityIntent

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Content(
        ultimateAnswer = ultimateAnswer,
        onMultiBindingClickButton = ::launchMultiBindingActivity,
        onCustomQualifierClickButton = ::launchCustomQualifierActivity,
        onAlliesMultiroundClickButton = ::launchAlliesActivity,
      )
    }
  }

  private fun launchMultiBindingActivity() {
    startActivity(MultiBindingActivity.intent(this))
  }

  private fun launchCustomQualifierActivity() {
    startActivity(CustomQualifierActivity.intent(this))
  }

  private fun launchAlliesActivity() {
    startActivity(alliesActivityIntent("Splinter", "April"))
  }
}

@Composable
private fun Content(
  ultimateAnswer: String,
  onMultiBindingClickButton: () -> Unit,
  onCustomQualifierClickButton: () -> Unit,
  onAlliesMultiroundClickButton: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = "The Answer to the Ultimate Question of Life, the Universe, and Everything is...")
    Spacer(modifier = Modifier.height(24.dp))
    Text(text = ultimateAnswer)
    Spacer(modifier = Modifier.height(48.dp))
    Button(onClick = { onMultiBindingClickButton() }) {
      Text(text = "Go to MultiBinding")
    }
    Spacer(modifier = Modifier.height(24.dp))
    Button(onClick = { onCustomQualifierClickButton() }) {
      Text(text = "Go to Qualifier")
    }
    Spacer(modifier = Modifier.height(24.dp))
    Button(onClick = { onAlliesMultiroundClickButton() }) {
      Text(text = "Go to Allies Multiple round")
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
  Content(
    ultimateAnswer = "42",
    onMultiBindingClickButton = {},
    onCustomQualifierClickButton = {},
    onAlliesMultiroundClickButton = {},
  )
}