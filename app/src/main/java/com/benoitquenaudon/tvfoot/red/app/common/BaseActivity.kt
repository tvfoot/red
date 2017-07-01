package com.benoitquenaudon.tvfoot.red.app.common

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatActivity
import com.benoitquenaudon.tvfoot.red.app.injection.ComponentFactory
import com.benoitquenaudon.tvfoot.red.app.injection.component.ActivityComponent
import com.benoitquenaudon.tvfoot.red.app.injection.component.ScreenComponent

abstract class BaseActivity : AppCompatActivity() {
  private val screenComponent: ScreenComponent by lazy {
    (lastCustomNonConfigurationInstance ?:
        ComponentFactory.screenComponent(this)) as ScreenComponent
  }
  val activityComponent: ActivityComponent by lazy {
    ComponentFactory.activityComponent(screenComponent, this)
  }

  @CallSuper override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onRetainCustomNonConfigurationInstance(): Any = screenComponent
}