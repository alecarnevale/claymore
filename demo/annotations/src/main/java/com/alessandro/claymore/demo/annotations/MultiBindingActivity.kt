package com.alessandro.claymore.demo.annotations

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alessandro.claymore.demo.annotations.models.NinjaTurtle
import com.alessandro.claymore.demo.annotations.services.ProvideNinjaTurtle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MultiBindingActivity : AppCompatActivity() {

  @Inject
  internal lateinit var provideNinjaTurtles: Set<@JvmSuppressWildcards ProvideNinjaTurtle>
  private val ninjaTurtles: List<NinjaTurtle> by lazy { provideNinjaTurtles.map { it() } }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      Content(ninjaTurtles = ninjaTurtles)
    }
  }

  companion object {
    fun intent(context: Context) = Intent(context, MultiBindingActivity::class.java)
  }
}

@Composable
private fun Content(
  ninjaTurtles: List<NinjaTurtle>
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize().padding(24.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    items(ninjaTurtles) {
      Column {
        Text(text = "Name ${it.name}")
        Text(text = "Bandana ${it.bandana}")
        Text(text = "Weapon ${it.weapon}")
      }
      Divider(Modifier.padding(vertical = 20.dp))
    }
  }
}

@Preview
@Composable
private fun Preview() {
  Content(
    ninjaTurtles = listOf(
      NinjaTurtle(
        name = "Leonardo",
        bandana = NinjaTurtle.Color.BLUE,
        weapon = NinjaTurtle.Weapon.Katana
      ),
      NinjaTurtle(
        name = "Donatello",
        bandana = NinjaTurtle.Color.PURPLE,
        weapon = NinjaTurtle.Weapon.Staff
      )
    )
  )
}