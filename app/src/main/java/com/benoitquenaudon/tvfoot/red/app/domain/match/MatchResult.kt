package com.benoitquenaudon.tvfoot.red.app.domain.match

import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.mvi.MviResult

sealed class MatchResult : MviResult {
  sealed class LoadMatchResult : MatchResult() {
    data class Success(val match: Match, val shouldNotifyMatchStart: Boolean) : LoadMatchResult()

    data class Failure(val throwable: Throwable) : LoadMatchResult()

    object InFlight : LoadMatchResult()
  }

  data class NotifyMatchStartResult(val shouldNotifyMatchStart: Boolean) : MatchResult()

  object GetLastStateResult : MatchResult()

  enum class Status {
    LOAD_MATCH_IN_FLIGHT, LOAD_MATCH_FAILURE, LOAD_MATCH_SUCCESS
  }
}
