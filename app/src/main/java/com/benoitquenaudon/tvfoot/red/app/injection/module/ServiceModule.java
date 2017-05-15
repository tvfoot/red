package com.benoitquenaudon.tvfoot.red.app.injection.module;

import dagger.Module;
import dagger.Provides;
import com.benoitquenaudon.tvfoot.red.api.TvfootService;
import javax.inject.Singleton;
import retrofit2.Retrofit;

@Module public class ServiceModule {
  @Provides @Singleton static TvfootService provideTvfootService(Retrofit retrofit) {
    return retrofit.create(TvfootService.class);
  }
}
