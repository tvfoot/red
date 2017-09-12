package com.benoitquenaudon.tvfoot.red.app.common

import android.support.v7.app.AppCompatActivity
import com.benoitquenaudon.tvfoot.red.injection.component.ActivityComponent
import com.benoitquenaudon.tvfoot.red.util.newActivityComponent

abstract class BaseActivity : AppCompatActivity() {
  val activityComponent: ActivityComponent by lazy {
    this.newActivityComponent()
  }
}