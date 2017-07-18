package com.benoitquenaudon.tvfoot.red.app.injection.module;

import android.app.Application;
import com.benoitquenaudon.tvfoot.red.app.common.firebase.BaseRedFirebaseAnalytics;
import com.benoitquenaudon.tvfoot.red.app.common.firebase.RedFirebaseAnalytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module public class FirebaseModule {
  @Provides @Singleton FirebaseAnalytics provideFirebaseAnalytics(Application context) {
    return FirebaseAnalytics.getInstance(context);
  }

  @Provides @Singleton BaseRedFirebaseAnalytics provideRedFirebaseAnalytics(
      FirebaseAnalytics firebaseAnalytics) {
    return new RedFirebaseAnalytics(firebaseAnalytics);
  }
}
