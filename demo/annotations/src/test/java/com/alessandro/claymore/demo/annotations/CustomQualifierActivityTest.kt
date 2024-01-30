package com.alessandro.claymore.demo.annotations

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.core.app.ActivityScenario
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
internal class CustomQualifierActivityTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Test
  fun `CustomQualifierActivity launched with fake weapons`() {
    ActivityScenario.launch(CustomQualifierActivity::class.java).use { scenario ->
      scenario.onActivity {
        composeTestRule.onNodeWithText("Leonardo wields a fake wooden katana").assertIsDisplayed()
        composeTestRule.onNodeWithText("Raffaello wields a fake nerfed sai").assertIsDisplayed()
        composeTestRule.onNodeWithText("Donatello wields a fake broken staff").assertIsDisplayed()
        composeTestRule.onNodeWithText("Michelangelo wields a fake rubber nunchaku").assertIsDisplayed()
      }
    }
  }
}