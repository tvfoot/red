package com.benoitquenaudon.tvfoot.red.app.common.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class RedFirebaseAnalytics(
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseRedFirebaseAnalytics {
  override fun logEvent(action: String, value: String) {
    val bundle = Bundle()
        .apply {
          putString(action, value)
        }
    firebaseAnalytics.logEvent(action, bundle)
  }
}