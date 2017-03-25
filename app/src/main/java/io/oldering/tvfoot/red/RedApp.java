package io.oldering.tvfoot.red;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import com.squareup.leakcanary.LeakCanary;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasDispatchingActivityInjector;
import io.oldering.tvfoot.red.di.component.AppComponent;
import io.oldering.tvfoot.red.di.component.DaggerAppComponent;
import io.oldering.tvfoot.red.di.module.AppModule;
import javax.inject.Inject;
import timber.log.Timber;

public class RedApp extends Application implements HasDispatchingActivityInjector {
  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
  AppComponent appComponent;

  public static RedApp get(Context context) {
    return (RedApp) context.getApplicationContext();
  }

  @Override public void onCreate() {
    super.onCreate();
    getComponent().inject(this);

    setupTimber();
    setupLeakCanary();
  }

  private void setupTimber() {
    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    }
  }

  private void setupLeakCanary() {
    if (LeakCanary.isInAnalyzerProcess(this)) {
      // This reduce is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this reduce.
      return;
    }
    LeakCanary.install(this);
    // Normal app init code...
  }

  public AppComponent getComponent() {
    if (appComponent == null) {
      appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }
    return appComponent;
  }

  @Override public DispatchingAndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }
}
