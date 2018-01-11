package com.benoitquenaudon.tvfoot.red.app.common.flowcontroller

import android.app.Activity
import android.content.Intent
import android.content.Intent.URI_INTENT_SCHEME
import android.net.Uri
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import javax.inject.Inject

class FlowController @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val activity: Activity
) {
  fun toMatches() {
    navigate(FlowIntentFactory.toMatchesIntent())
  }

  fun toMatchForResult(matchId: String, requestCode: Int) {
    navigateForResult(FlowIntentFactory.toMatchIntent(matchId), requestCode)
  }

  fun toSettings() {
    navigate(FlowIntentFactory.toSettingsIntent())
  }

  private fun navigate(intent: Intent) {
    logNavigation(intent)
    activity.startActivity(intent)
  }

  private fun navigateForResult(intent: Intent, requestCode: Int) {
    logNavigation(intent)
    activity.startActivityForResult(intent, requestCode)
  }

  private fun logNavigation(intent: Intent) {
    val params = Bundle()
    params.putString("navigation_intent", intent.toUri(URI_INTENT_SCHEME))
    firebaseAnalytics.logEvent("navigation", params)
  }

  fun toLibrary(link: String) {
    val intent = Intent(Intent.ACTION_VIEW).also { it.data = Uri.parse(link) }
    activity.startActivity(intent)
  }
}
