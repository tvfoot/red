package com.benoitquenaudon.tvfoot.red.util

import android.app.Activity
import com.benoitquenaudon.tvfoot.red.RedApp
import com.benoitquenaudon.tvfoot.red.injection.component.ActivityComponent
import com.benoitquenaudon.tvfoot.red.injection.component.ScreenComponent
import com.benoitquenaudon.tvfoot.red.injection.module.ActivityModule

fun Activity.newScreenComponent(): ScreenComponent {
  return RedApp.getApp(this).appComponent.screenComponent()
}

fun Activity.newActivityComponent(screenComponent: ScreenComponent): ActivityComponent {
  return screenComponent.plus(ActivityModule(this))
}