package com.benoitquenaudon.tvfoot.red.app.injection.module

import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module object SchedulerModule {

  @JvmStatic @Provides @Singleton fun provideSchedulerProvider(): BaseSchedulerProvider {
    return SchedulerProvider()
  }
}
