package com.benoitquenaudon.tvfoot.red.app.injection.module;

import dagger.Module;
import dagger.Provides;
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider;
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.ImmediateSchedulerProvider;
import javax.inject.Singleton;

@Module public class TestSchedulerModule {

  @Provides @Singleton BaseSchedulerProvider provideSchedulerProvider() {
    return new ImmediateSchedulerProvider();
  }
}
