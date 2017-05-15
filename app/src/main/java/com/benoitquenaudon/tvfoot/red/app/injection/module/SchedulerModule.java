package com.benoitquenaudon.tvfoot.red.app.injection.module;

import dagger.Module;
import dagger.Provides;
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider;
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.SchedulerProvider;
import javax.inject.Singleton;

@Module public class SchedulerModule {
  @Provides @Singleton BaseSchedulerProvider provideSchedulerProvider() {
    return new SchedulerProvider();
  }
}
