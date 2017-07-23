package com.benoitquenaudon.tvfoot.red.app.domain.match.state

import com.benoitquenaudon.tvfoot.red.app.mvi.MviAction

sealed class MatchAction : MviAction {
  data class LoadMatchAction(val matchId: String) : MatchAction()

  data class NotifyMatchStartAction(
      val matchId: String,
      val startAt: Long,
      val notifyMatchStart: Boolean
  ) : MatchAction()

  object GetLastStateAction : MatchAction()
}
