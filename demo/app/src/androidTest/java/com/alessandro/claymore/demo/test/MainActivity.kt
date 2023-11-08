package com.alessandro.claymore.demo.test

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Test

class MainActivity {
  @Test
  fun testPackageName() {
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext
    assertEquals("com.alessandro.claymore.demo", appContext.packageName)
  }
}