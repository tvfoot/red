package com.benoitquenaudon.tvfoot.red

import com.benoitquenaudon.tvfoot.red.injection.component.DaggerAppComponent
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class RedApp : DaggerApplication() {
  override fun applicationInjector(): AndroidInjector<RedApp> {
    return DaggerAppComponent.builder().create(this)
  }

  override fun onCreate() {
    super.onCreate()

    setupLeakCanary()
    setupTimber()
  }

  private fun setupTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(Timber.DebugTree())
    }
  }

  private fun setupLeakCanary() {
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This reduce is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this reduce.
      return
    }
    LeakCanary.install(this)
  }
}
