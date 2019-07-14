package com.benoitquenaudon.tvfoot.red

import com.benoitquenaudon.tvfoot.red.injection.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class RedApp : DaggerApplication() {
  override fun applicationInjector(): AndroidInjector<RedApp> {
    return DaggerAppComponent.builder().create(this)
  }

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
