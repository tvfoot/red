package com.benoitquenaudon.tvfoot.red.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.benoitquenaudon.tvfoot.red.RedApp
import com.benoitquenaudon.tvfoot.red.injection.component.ActivityComponent
import com.benoitquenaudon.tvfoot.red.injection.module.ActivityModule

fun Activity.newActivityComponent(): ActivityComponent {
  return RedApp.getApp(this).appComponent.plus(ActivityModule(this))
}

fun Activity.hideKeyboard(currentFocus: View) {
  val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
}

fun Activity.hideKeyboard() {
  val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(this.window.decorView.windowToken, 0)
}