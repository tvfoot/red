package com.benoitquenaudon.tvfoot.red.app.common

import android.view.View
import android.widget.TextView
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

class DataBindingAdaptersTest {
  @Test fun setVisibility_shouldBeGone() {
    val view = mock(View::class.java)
    setVisibility(view, isVisible = false)
    verify(view).visibility = View.GONE
  }

  @Test fun setVisibility_shouldBeVisible() {
    val view = mock(View::class.java)
    setVisibility(view, isVisible = true)
    verify(view).visibility = View.VISIBLE
  }

  @Test fun setDangerText_shouldNotSet() {
    val textView = mock(TextView::class.java)
    setDangerText(textView, textResId = 0)
    verify(textView, never()).setText(0)
  }

  @Test fun setDangerText_shouldSet() {
    val textView = mock(TextView::class.java)
    setDangerText(textView, textResId = 1)
    verify(textView).setText(1)
  }
}
