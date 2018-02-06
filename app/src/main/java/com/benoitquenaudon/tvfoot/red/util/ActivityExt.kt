package com.benoitquenaudon.tvfoot.red.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.content.systemService

fun Activity.hideKeyboard(currentFocus: View) {
  this.systemService<InputMethodManager>()
      .hideSoftInputFromWindow(currentFocus.windowToken, 0)
}

fun Activity.hideKeyboard() {
  this.systemService<InputMethodManager>()
      .hideSoftInputFromWindow(this.window.decorView.windowToken, 0)
}