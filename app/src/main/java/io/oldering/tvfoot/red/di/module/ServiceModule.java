package io.oldering.tvfoot.red.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.api.MatchService;
import retrofit2.Retrofit;

@Module
public class ServiceModule {

    @Provides
    @Singleton
    MatchService provideMatchService(Retrofit retrofit) {
        return retrofit.create(MatchService.class);
    }
}
