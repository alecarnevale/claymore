package com.alessandro.claymore.demo

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ActivityScenario
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun `AutoBinds annotation provides FooImpl`() {
    ActivityScenario.launch(MainActivity::class.java).use { scenario ->
      scenario.onActivity {
        composeTestRule.onNodeWithText("This is FooImpl, provided by: FooProviderImpl").assertIsDisplayed()
      }
    }
  }

  @Test
  fun `AutoBinds annotation provides BarImpl`() {
    ActivityScenario.launch(MainActivity::class.java).use { scenario ->
      scenario.onActivity {
        composeTestRule.onNodeWithText("This is BarImpl, provided by: BarProviderImpl").assertIsDisplayed()
      }
    }
  }

  @Test
  fun `Multibindings annotation provided`() {
    ActivityScenario.launch(MainActivity::class.java).use { scenario ->
      scenario.onActivity {
        composeTestRule.onNodeWithText("This is BazImpl, provided by: BazProviderImpl1").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is BazImpl, provided by: BazProviderImpl2").assertIsDisplayed()
        composeTestRule.onNodeWithText("This is BazImpl, provided by: BazProviderImpl3").assertIsDisplayed()
      }
    }
  }
}