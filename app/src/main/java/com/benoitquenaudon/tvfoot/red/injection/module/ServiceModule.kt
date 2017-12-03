package com.benoitquenaudon.tvfoot.red.injection.module

import android.app.AlarmManager
import android.app.Application
import android.content.Context
import com.benoitquenaudon.tvfoot.red.api.TvfootService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
object ServiceModule {
  @JvmStatic
  @Provides
  @Singleton
  fun provideTvfootService(retrofit: Retrofit): TvfootService {
    return retrofit.create(TvfootService::class.java)
  }

  @JvmStatic
  @Provides
  fun provideAlarmManager(context: Application): AlarmManager {
    return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
  }
}
