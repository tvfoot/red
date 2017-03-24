package io.oldering.tvfoot.red.di.module;

import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.data.api.TvfootService;
import javax.inject.Singleton;
import retrofit2.Retrofit;

@Module public class ServiceModule {
  @Provides @Singleton TvfootService provideTvfootService(Retrofit retrofit) {
    return retrofit.create(TvfootService.class);
  }
}
