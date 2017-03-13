package io.oldering.tvfoot.red;

import android.app.Application;
import android.content.Context;
import com.squareup.leakcanary.LeakCanary;
import io.oldering.tvfoot.red.api.MatchService;
import io.oldering.tvfoot.red.di.component.AppComponent;
import io.oldering.tvfoot.red.di.component.DaggerAppComponent;
import io.oldering.tvfoot.red.di.module.AppModule;
import io.oldering.tvfoot.red.di.module.NetworkModule;
import timber.log.Timber;

public class RedApp extends Application {
  AppComponent appComponent;

  public static RedApp get(Context context) {
    return (RedApp) context.getApplicationContext();
  }

  @Override public void onCreate() {
    super.onCreate();

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
      // This process is dedicated to LeakCanary for heap analysis.
      // You should not init your app in this process.
      return;
    }
    LeakCanary.install(this);
    // Normal app init code...
  }

  public AppComponent getComponent() {
    if (appComponent == null) {
      appComponent = DaggerAppComponent.builder()
          .appModule(new AppModule(this))
          .networkModule(new NetworkModule(MatchService.BASE_URL))
          .build();
    }
    return appComponent;
  }
}
