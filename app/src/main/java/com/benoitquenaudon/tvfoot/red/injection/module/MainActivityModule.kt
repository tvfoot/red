package com.benoitquenaudon.tvfoot.red.injection.module

import android.app.Activity
import com.benoitquenaudon.tvfoot.red.app.domain.main.MainActivity
import dagger.Binds
import dagger.Module

@Module
abstract class MainActivityModule {
  @Binds abstract fun provideActivity(activity: MainActivity): Activity
}