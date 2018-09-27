package com.benoitquenaudon.tvfoot.red.app.common.flowcontroller

import android.app.Activity
import android.content.Intent
import androidx.net.toUri
import javax.inject.Inject

class FlowController @Inject constructor(
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
    activity.startActivity(intent)
  }

  private fun navigateForResult(intent: Intent, requestCode: Int) {
    activity.startActivityForResult(intent, requestCode)
  }

  fun toLibrary(link: String) {
    val intent = Intent(Intent.ACTION_VIEW).also { it.data = link.toUri() }
    activity.startActivity(intent)
  }
}
