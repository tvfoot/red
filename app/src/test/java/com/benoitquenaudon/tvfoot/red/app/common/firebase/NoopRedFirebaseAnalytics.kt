package com.benoitquenaudon.tvfoot.red.app.common.firebase

object NoopRedFirebaseAnalytics : BaseRedFirebaseAnalytics {
  override fun logEvent(action: String, value: String) {
    // noop
  }
}