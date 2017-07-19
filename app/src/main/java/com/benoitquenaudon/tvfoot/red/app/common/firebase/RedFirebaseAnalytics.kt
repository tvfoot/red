package com.benoitquenaudon.tvfoot.red.app.common.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class RedFirebaseAnalytics(val firebaseAnalytics: FirebaseAnalytics) : BaseRedFirebaseAnalytics {
  override fun logEvent(string: String, bundle: Bundle) {
    firebaseAnalytics.logEvent(string, bundle)
  }
}