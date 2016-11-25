package io.oldering.tvfoot.red.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.util.schedulers.BaseSchedulerProvider;
import io.oldering.tvfoot.red.util.schedulers.ImmediateSchedulerProvider;

@Module
public class TestSchedulerModule {

    @Provides
    @Singleton
    BaseSchedulerProvider provideSchedulerProvider() {
        return new ImmediateSchedulerProvider();
    }
}
