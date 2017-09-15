package com.benoitquenaudon.tvfoot.red.app.domain.match

import com.benoitquenaudon.tvfoot.red.app.mvi.MviIntent

sealed class MatchIntent : MviIntent {
  data class InitialIntent(val matchId: String) : MatchIntent()

  data class NotifyMatchStartIntent(
      val matchId: String,
      val startAt: Long,
      val notifyMatchStart: Boolean
  ) : MatchIntent()

  object GetLastState : MatchIntent()
}
