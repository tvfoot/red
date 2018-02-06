package com.benoitquenaudon.tvfoot.red.app.common.firebase

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class RedFirebaseAnalytics(
    private val firebaseAnalytics: FirebaseAnalytics
) : BaseRedFirebaseAnalytics {
  override fun logEvent(string: String, vararg pairs: Pair<String, String>) {
    Bundle().apply {
      for ((key, value) in pairs) {
        putString(key, value)
      }
    }.also {
          firebaseAnalytics.logEvent(string, it)
        }
  }
}