package io.oldering.tvfoot.red.di.module;

import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.data.api.MatchService;
import io.oldering.tvfoot.red.util.FakeMatchService;
import io.oldering.tvfoot.red.util.Fixture;
import javax.inject.Singleton;

@Module public class ServiceMockModule {
  @Provides @Singleton MatchService provideMatchService(Fixture fixture) {
    return new FakeMatchService(fixture);
  }
}