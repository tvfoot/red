package com.benoitquenaudon.tvfoot.red.app.common

import android.support.v7.app.AppCompatActivity
import com.benoitquenaudon.tvfoot.red.injection.component.ActivityComponent
import com.benoitquenaudon.tvfoot.red.injection.component.ScreenComponent
import com.benoitquenaudon.tvfoot.red.util.newActivityComponent
import com.benoitquenaudon.tvfoot.red.util.newScreenComponent

abstract class BaseActivity : AppCompatActivity() {
  private val screenComponent: ScreenComponent by lazy {
    (lastCustomNonConfigurationInstance ?: this.newScreenComponent()) as ScreenComponent
  }

  val activityComponent: ActivityComponent by lazy {
    this.newActivityComponent(screenComponent)
  }

  override fun onRetainCustomNonConfigurationInstance(): Any = screenComponent
}