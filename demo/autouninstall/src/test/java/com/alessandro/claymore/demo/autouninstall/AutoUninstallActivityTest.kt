package com.alessandro.claymore.demo.autouninstall

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
internal class AutoUninstallActivityTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Test
  fun `AutoUninstallActivity launched with mocked services`() {
    ActivityScenario.launch(AutoUninstallActivity::class.java).use { scenario ->
      scenario.onActivity {
        composeTestRule.onNodeWithText("FakeService").assertIsDisplayed()
        composeTestRule.onNodeWithText("FakeOtherService").assertIsDisplayed()
      }
    }
  }
}