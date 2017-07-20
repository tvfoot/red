package com.benoitquenaudon.tvfoot.red.app.domain.match.state

sealed class MatchIntent {
  data class InitialIntent(val matchId: String) : MatchIntent()

  data class NotifyMatchStartIntent(
      val matchId: String,
      val startAt: Long,
      val notifyMatchStart: Boolean
  ) : MatchIntent()

  object GetLastState : MatchIntent()
}
