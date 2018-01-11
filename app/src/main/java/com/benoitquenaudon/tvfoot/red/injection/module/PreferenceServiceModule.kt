package com.benoitquenaudon.tvfoot.red.injection.module

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.benoitquenaudon.tvfoot.red.RedApp
import dagger.Module
import dagger.Provides

@Module
object PreferenceServiceModule {

  @JvmStatic
  @Provides
  fun provideSharedPreferences(context: RedApp): SharedPreferences {
    return context.getSharedPreferences("Red", MODE_PRIVATE)
  }
}
