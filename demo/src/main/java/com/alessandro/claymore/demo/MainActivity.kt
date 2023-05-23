package com.alessandro.claymore.demo

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
import com.alessandro.claymore.demo.modelproviders.BarProvider
import com.alessandro.claymore.demo.modelproviders.FooProvider
import com.alessandro.claymore.demo.models.Bar
import com.alessandro.claymore.demo.models.Foo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  @Inject
  lateinit var fooProvider: FooProvider
  private val foo: Foo by lazy { fooProvider.get() }

  @Inject
  lateinit var barProvider: BarProvider
  private val bar: Bar by lazy { barProvider.get() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Content(
        fooString = foo.get(),
        barString = bar.get()
      )
    }
  }
}

@Composable
private fun Content(
  fooString: String,
  barString: String
) {
  Column(
    modifier = Modifier
      .fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(text = fooString)
    Spacer(modifier = Modifier.height(50.dp))
    Text(text = barString)
  }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
  Content(
    fooString = "Foo string",
    barString = "Bar string"
  )
}