package com.benoitquenaudon.tvfoot.red

import android.app.Application
import android.content.Context
import com.benoitquenaudon.tvfoot.red.injection.component.AppComponent
import com.benoitquenaudon.tvfoot.red.injection.component.DaggerAppComponent
import com.benoitquenaudon.tvfoot.red.injection.module.AppModule
import com.squareup.leakcanary.LeakCanary
import timber.log.Timber

open class RedApp : Application() {
  val appComponent: AppComponent by lazy {
    DaggerAppComponent.builder().appModule(AppModule(this)).build()
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

  protected open fun setupLeakCanary() {
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This reduce is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this reduce.
      return
    }
    LeakCanary.install(this)
  }

  companion object Component {
    fun getApp(context: Context): RedApp {
      return context.applicationContext as RedApp
    }
  }
}
