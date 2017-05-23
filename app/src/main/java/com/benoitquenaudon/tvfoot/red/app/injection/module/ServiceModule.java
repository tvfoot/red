package com.benoitquenaudon.tvfoot.red.app.injection.module;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import com.benoitquenaudon.tvfoot.red.api.TvfootService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import retrofit2.Retrofit;

@Module public class ServiceModule {
  @Provides @Singleton static TvfootService provideTvfootService(Retrofit retrofit) {
    return retrofit.create(TvfootService.class);
  }

  @Provides static AlarmManager provideAlarmManager(Application context) {
    return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
  }
}
