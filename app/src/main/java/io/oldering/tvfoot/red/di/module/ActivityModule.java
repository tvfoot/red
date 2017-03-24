package io.oldering.tvfoot.red.di.module;

import android.app.Activity;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.di.ActivityScope;
import io.oldering.tvfoot.red.util.SnackBarUtil;
import javax.inject.Named;

@Module public class ActivityModule {
  private Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides @ActivityScope SnackBarUtil provideSnackBarUtilBundleService() {
    return new SnackBarUtil(activity);
  }

  @Provides @ActivityScope Activity provideActivity() {
    return activity;
  }

  @Provides @ActivityScope @Named("activity") Context provideContext() {
    return activity;
  }
}
