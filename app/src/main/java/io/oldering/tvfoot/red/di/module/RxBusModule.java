package io.oldering.tvfoot.red.di.module;

import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.util.rxbus.RxBus;
import javax.inject.Singleton;

@Module public class RxBusModule {

  @Provides @Singleton RxBus provideRxBus() {
    return new RxBus();
  }
}
