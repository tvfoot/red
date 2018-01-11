package com.benoitquenaudon.tvfoot.red.injection.module

import android.app.Activity
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchActivity
import dagger.Binds
import dagger.Module

@Module
abstract class MatchActivityModule {
  @Binds abstract fun provideActivity(activity: MatchActivity): Activity
}