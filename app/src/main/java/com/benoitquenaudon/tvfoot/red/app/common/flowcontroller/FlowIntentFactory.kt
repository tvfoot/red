package com.benoitquenaudon.tvfoot.red.app.common.flowcontroller

import android.content.Intent
import androidx.core.net.toUri

object FlowIntentFactory {
  fun toMatchesIntent(): Intent = Intent(Intent.ACTION_VIEW, "tvfoot://tvfoot/".toUri())

  // We don't need to set the in between 'league', 'home' and 'away' strings.
  // They can be anything.
  fun toMatchIntent(matchId: String): Intent =
      Intent(Intent.ACTION_VIEW, "tvfoot://tvfoot/match/league/home/away/$matchId".toUri())

  fun toSettingsIntent(): Intent = Intent(Intent.ACTION_VIEW, "tvfoot://tvfoot/settings".toUri())
}