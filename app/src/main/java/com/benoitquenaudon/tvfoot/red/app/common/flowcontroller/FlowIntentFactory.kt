package com.benoitquenaudon.tvfoot.red.app.common.flowcontroller

import android.content.Intent
import android.net.Uri

object FlowIntentFactory {
  fun toMatchesIntent(): Intent = Intent(Intent.ACTION_VIEW, Uri.parse("tvfoot://tvfoot/"))

  // We don't need to set the in between 'league', 'home' and 'away' strings.
  // They can be anything.
  fun toMatchIntent(matchId: String): Intent = Intent(Intent.ACTION_VIEW,
      Uri.parse("tvfoot://tvfoot/match/league/home/away/$matchId"))

  fun toSettingsIntent(): Intent = Intent(Intent.ACTION_VIEW, Uri.parse("tvfoot://tvfoot/settings"))
}