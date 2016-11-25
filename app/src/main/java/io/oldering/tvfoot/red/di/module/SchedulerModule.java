package io.oldering.tvfoot.red.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.util.schedulers.BaseSchedulerProvider;
import io.oldering.tvfoot.red.util.schedulers.SchedulerProvider;

@Module
public class SchedulerModule {

    @Provides
    @Singleton
    BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}
