package com.benoitquenaudon.tvfoot.red.app.common.firebase

object NoopRedFirebaseAnalytics : BaseRedFirebaseAnalytics {
  override fun logEvent(string: String, vararg pairs: Pair<String, String>) {
    // noop
  }
}