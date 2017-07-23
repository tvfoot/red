package com.benoitquenaudon.tvfoot.red.app.injection.module

import android.app.Activity
import com.benoitquenaudon.tvfoot.red.app.injection.scope.ActivityScope
import dagger.Module
import dagger.Provides

@Module class ActivityModule(val activity: Activity) {

  @Provides @ActivityScope fun provideActivity(): Activity {
    return activity
  }
}