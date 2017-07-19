package com.benoitquenaudon.tvfoot.red.util

import android.app.Activity
import com.benoitquenaudon.tvfoot.red.RedApp
import com.benoitquenaudon.tvfoot.red.app.injection.component.ActivityComponent
import com.benoitquenaudon.tvfoot.red.app.injection.component.ScreenComponent
import com.benoitquenaudon.tvfoot.red.app.injection.module.ActivityModule

fun Activity.newScreenComponent(): ScreenComponent {
  return RedApp.get(this).component.screenComponent()
}

fun Activity.newActivityComponent(screenComponent: ScreenComponent): ActivityComponent {
  return screenComponent.plus(ActivityModule(this))
}