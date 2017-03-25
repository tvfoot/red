package io.oldering.tvfoot.red.di.module;

import android.app.Activity;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.util.SnackBarUtil;
import javax.inject.Named;

@Module public class ActivityModule {
  private Activity activity;

  public ActivityModule(Activity activity) {
    this.activity = activity;
  }

  @Provides SnackBarUtil provideSnackBarUtilBundleService() {
    return new SnackBarUtil(activity);
  }

  @Provides Activity provideActivity() {
    return activity;
  }

  @Provides @Named("activity") Context provideContext() {
    return activity;
  }
}
