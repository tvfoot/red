package com.benoitquenaudon.tvfoot.red.util

import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/**
 * There was a working implementation that failed on Android so I moved the tests inside AndroidTest
 */
@RunWith(AndroidJUnit4::class)
class StringExtAndroidTest {
  @Test
  fun stripAccents() {
    assertEquals("grece", "grèce".stripAccents())
    assertEquals("azerbaidjan", "azerbaïdjan".stripAccents())
  }
}