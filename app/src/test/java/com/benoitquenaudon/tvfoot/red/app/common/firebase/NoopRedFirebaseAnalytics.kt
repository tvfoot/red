package com.benoitquenaudon.tvfoot.red.app.common.firebase

import android.os.Bundle

object NoopRedFirebaseAnalytics : BaseRedFirebaseAnalytics {
  override fun logEvent(string: String, bundle: Bundle) {
    // noop
  }
}