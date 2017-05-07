package io.oldering.tvfoot.red.app.injection.module;

import android.app.Application;
import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.api.TvfootService;
import javax.inject.Singleton;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

@Module public class ServiceModule {
  @Provides @Singleton static TvfootService provideTvfootService(Retrofit retrofit) {
    return retrofit.create(TvfootService.class);
  }

  @Provides static SharedPreferences provideSharedPreferences(Application context) {
    return context.getSharedPreferences("Red", MODE_PRIVATE);
  }
}
