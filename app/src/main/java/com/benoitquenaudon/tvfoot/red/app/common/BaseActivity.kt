package com.benoitquenaudon.tvfoot.red.app.common

import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.benoitquenaudon.tvfoot.red.injection.component.ActivityComponent
import com.benoitquenaudon.tvfoot.red.util.newActivityComponent

abstract class BaseActivity : AppCompatActivity() {
  val activityComponent: ActivityComponent by lazy {
    this.newActivityComponent()
  }

  override fun onOptionsItemSelected(item: MenuItem) =
      when (item.itemId) {
        android.R.id.home -> {
          finish()
          true
        }
        else -> super.onOptionsItemSelected(item)
      }
}