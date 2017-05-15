package com.benoitquenaudon.tvfoot.red.app.injection.module;

import android.app.Application;
import com.google.firebase.analytics.FirebaseAnalytics;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class FirebaseModule {
  @Provides @Singleton FirebaseAnalytics provideFirebaseAnalytics(Application context) {
    return FirebaseAnalytics.getInstance(context);
  }
}
