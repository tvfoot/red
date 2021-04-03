package com.benoitquenaudon.tvfoot.red

import android.app.Application
import timber.log.Timber

class RedApp : Application() {
  override fun onCreate() {
    super.onCreate()

    setupTimber()
  }

  private fun setupTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }
}
