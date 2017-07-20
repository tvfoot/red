package com.benoitquenaudon.tvfoot.red.app.domain.match.state

sealed class MatchAction {
  data class LoadMatchAction(val matchId: String) : MatchAction()

  data class NotifyMatchStartAction(
      val matchId: String,
      val startAt: Long,
      val notifyMatchStart: Boolean
  ) : MatchAction()

  object GetLastStateAction : MatchAction()
}
