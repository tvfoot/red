package com.benoitquenaudon.tvfoot.red.app.injection.module;

import dagger.Module;
import dagger.Provides;
import com.benoitquenaudon.tvfoot.red.api.TvfootService;
import com.benoitquenaudon.tvfoot.red.util.FakeTvfootService;
import com.benoitquenaudon.tvfoot.red.util.Fixture;
import javax.inject.Singleton;

@Module public class ServiceMockModule {
  @Provides @Singleton TvfootService provideMatchService(Fixture fixture) {
    return new FakeTvfootService(fixture);
  }
}