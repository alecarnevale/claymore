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
  fun `AutoBinds annotation provides 42 as the right answer`() {
    ActivityScenario.launch(MainActivity::class.java).use { scenario ->
      scenario.onActivity {
        with(composeTestRule) {
          onNodeWithText("The Answer to the Ultimate Question of Life, the Universe, and Everything is...").assertIsDisplayed()
          onNodeWithText("42").assertIsDisplayed()
        }
      }
    }
  }
}