package com.benoitquenaudon.tvfoot.red.injection.module

import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.ImmediateSchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module object TestSchedulerModule {

  @JvmStatic @Provides @Singleton fun provideSchedulerProvider(): BaseSchedulerProvider {
    return ImmediateSchedulerProvider()
  }
}
