package com.benoitquenaudon.tvfoot.red.app.common

import android.view.MenuItem
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {
  override fun onOptionsItemSelected(item: MenuItem) =
      when (item.itemId) {
        android.R.id.home -> {
          finish()
          true
        }
        else -> super.onOptionsItemSelected(item)
      }
}