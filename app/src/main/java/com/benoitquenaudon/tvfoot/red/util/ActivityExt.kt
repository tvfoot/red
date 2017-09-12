package com.benoitquenaudon.tvfoot.red.util

import android.app.Activity
import com.benoitquenaudon.tvfoot.red.RedApp
import com.benoitquenaudon.tvfoot.red.injection.component.ActivityComponent
import com.benoitquenaudon.tvfoot.red.injection.module.ActivityModule

fun Activity.newActivityComponent(): ActivityComponent {
  return RedApp.getApp(this).appComponent.plus(ActivityModule(this))
}