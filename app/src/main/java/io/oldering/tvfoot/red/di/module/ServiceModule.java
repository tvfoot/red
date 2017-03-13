package io.oldering.tvfoot.red.di.module;

import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.data.api.MatchService;
import javax.inject.Singleton;
import retrofit2.Retrofit;

@Module public class ServiceModule {

  @Provides @Singleton MatchService provideMatchService(Retrofit retrofit) {
    return retrofit.create(MatchService.class);
  }
}
