package com.benoitquenaudon.tvfoot.red.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard(currentFocus: View) {
  (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
      .hideSoftInputFromWindow(currentFocus.windowToken, 0)
}

fun Activity.hideKeyboard() {
  (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
      .hideSoftInputFromWindow(this.window.decorView.windowToken, 0)
}