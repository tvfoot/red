package com.benoitquenaudon.tvfoot.red.injection.module

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module object PreferenceServiceModule {

  @JvmStatic @Provides fun provideSharedPreferences(context: Application): SharedPreferences {
    return context.getSharedPreferences("Red", MODE_PRIVATE)
  }
}
