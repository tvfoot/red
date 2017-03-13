package io.oldering.tvfoot.red.di.module;

import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.util.schedulers.BaseSchedulerProvider;
import io.oldering.tvfoot.red.util.schedulers.ImmediateSchedulerProvider;
import javax.inject.Singleton;

@Module public class TestSchedulerModule {

  @Provides @Singleton BaseSchedulerProvider provideSchedulerProvider() {
    return new ImmediateSchedulerProvider();
  }
}
