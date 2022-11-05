package com.alessandro.claymore.demo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.alessandro.claymore.demo.modelproviders.FooProvider
import com.alessandro.claymore.demo.models.Foo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var fooProvider: FooProvider
  private val foo: Foo by lazy { fooProvider.get() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Content(foo.get())
    }
  }
}

@Composable
private fun Content(
  modelString: String
) {
  Box(
    modifier = Modifier
      .fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Text(text = modelString)
  }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
  Content("Hello world!")
}