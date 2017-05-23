package com.benoitquenaudon.tvfoot.red.app.injection.module;

import android.app.Activity;
import com.benoitquenaudon.tvfoot.red.app.injection.scope.ActivityScope;
import dagger.Module;
import dagger.Provides;

@Module public class ActivityModule {
  private Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides @ActivityScope Activity provideActivity() {
    return activity;
  }
}