package io.oldering.tvfoot.red.app.injection.module;

import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.app.common.schedulers.BaseSchedulerProvider;
import io.oldering.tvfoot.red.app.common.schedulers.SchedulerProvider;
import javax.inject.Singleton;

@Module public class SchedulerModule {
  @Provides @Singleton BaseSchedulerProvider provideSchedulerProvider() {
    return new SchedulerProvider();
  }
}
