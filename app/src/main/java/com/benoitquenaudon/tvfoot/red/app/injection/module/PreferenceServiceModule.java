package com.benoitquenaudon.tvfoot.red.app.injection.module;

import android.app.Application;
import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;

import static android.content.Context.MODE_PRIVATE;

@Module public class PreferenceServiceModule {
  @Provides static SharedPreferences provideSharedPreferences(Application context) {
    return context.getSharedPreferences("Red", MODE_PRIVATE);
  }
}
