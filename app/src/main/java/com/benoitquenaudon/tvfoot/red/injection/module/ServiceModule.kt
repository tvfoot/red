package com.benoitquenaudon.tvfoot.red.injection.module

import android.app.AlarmManager
import androidx.content.systemService
import com.benoitquenaudon.tvfoot.red.RedApp
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
  fun provideAlarmManager(context: RedApp): AlarmManager {
    return context.systemService()
  }
}
