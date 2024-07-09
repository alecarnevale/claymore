package com.alessandro.claymore.demo.multiround.villains

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import com.alessandro.claymore.demo.multiround.models.Villain
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
class VillainsActivityTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var villainsActivityIntent: VillainsActivityIntent

  @Before
  fun setup() {
    hiltRule.inject()
  }

  @Test
  fun `Villains appear`() {
    ActivityScenario.launch<VillainsActivity>(villainsActivityIntent(Villain("Shredder"), Villain("Karai"))).use { scenario ->
      scenario.onActivity {
        with(composeTestRule) {
          onNodeWithText("Waiting for the first villain...").assertIsDisplayed()
          onNodeWithText("Waiting for the second villain...").assertIsDisplayed()
          onNodeWithText("Release villains").performClick()

          onNodeWithText("Shredder appears!").assertIsDisplayed()
          onNodeWithText("Karai appears!").assertIsDisplayed()
        }
      }
    }
  }
}