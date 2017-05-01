package io.oldering.tvfoot.red.app.injection.module;

import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.api.TvfootService;
import io.oldering.tvfoot.red.util.FakeTvfootService;
import io.oldering.tvfoot.red.util.Fixture;
import javax.inject.Singleton;

@Module public class ServiceMockModule {
  @Provides @Singleton TvfootService provideMatchService(Fixture fixture) {
    return new FakeTvfootService(fixture);
  }
}