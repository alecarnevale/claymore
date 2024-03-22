package com.alessandro.claymore.demo.multiround.allies

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
@HiltAndroidTest
class AlliesActivityTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var alliesActivityIntent: AlliesActivityIntent

  @Before
  fun setup() {
    hiltRule.inject()
  }

  @Test
  fun `Allies appear`() {
    ActivityScenario.launch<AlliesActivity>(alliesActivityIntent("Splinter", "April")).use { scenario ->
      scenario.onActivity {
        with(composeTestRule) {
          onNodeWithText("Waiting for the first ally...").assertIsDisplayed()
          onNodeWithText("Waiting for the second ally...").assertIsDisplayed()
          onNodeWithText("Release allies").performClick()

          onNodeWithText("Splinter appears!").assertIsDisplayed()
          onNodeWithText("April appears!").assertIsDisplayed()
        }
      }
    }
  }
}