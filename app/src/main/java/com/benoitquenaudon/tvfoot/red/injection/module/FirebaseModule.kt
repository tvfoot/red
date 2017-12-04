package com.benoitquenaudon.tvfoot.red.injection.module

import android.app.Application
import com.benoitquenaudon.tvfoot.red.app.common.firebase.BaseRedFirebaseAnalytics
import com.benoitquenaudon.tvfoot.red.app.common.firebase.RedFirebaseAnalytics
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object FirebaseModule {

  @JvmStatic
  @Provides
  @Singleton
  fun provideFirebaseAnalytics(context: Application): FirebaseAnalytics {
    return FirebaseAnalytics.getInstance(context)
  }

  @JvmStatic
  @Provides
  @Singleton
  fun provideRedFirebaseAnalytics(firebaseAnalytics: FirebaseAnalytics): BaseRedFirebaseAnalytics {
    return RedFirebaseAnalytics(firebaseAnalytics)
  }
}
