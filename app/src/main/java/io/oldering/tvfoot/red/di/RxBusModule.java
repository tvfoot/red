package io.oldering.tvfoot.red.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.util.rxbus.RxBus;

@Module
public class RxBusModule {

    @Provides
    @Singleton
    RxBus provideRxBus() {
        return new RxBus();
    }
}
