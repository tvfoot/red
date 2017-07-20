package com.benoitquenaudon.tvfoot.red.app.domain.match.state

import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchResult.Status.LOAD_MATCH_FAILURE
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchResult.Status.LOAD_MATCH_IN_FLIGHT
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchResult.Status.LOAD_MATCH_SUCCESS

sealed class MatchResult {
  @Suppress("DataClassPrivateConstructor")
  data class LoadMatchResult private constructor(
      val status: Status,
      val match: Match?,
      val error: Throwable?,
      val shouldNotifyMatchStart: Boolean
  ) : MatchResult() {
    companion object {
      fun success(match: Match, shouldNotifyMatchStart: Boolean): LoadMatchResult {
        return LoadMatchResult(LOAD_MATCH_SUCCESS, match, null, shouldNotifyMatchStart)
      }

      fun failure(throwable: Throwable): LoadMatchResult {
        return LoadMatchResult(LOAD_MATCH_FAILURE, null, throwable, false)
      }

      fun inFlight(): LoadMatchResult {
        return LoadMatchResult(LOAD_MATCH_IN_FLIGHT, null, null, false)
      }
    }
  }

  data class NotifyMatchStartResult(val shouldNotifyMatchStart: Boolean) : MatchResult()

  object GetLastStateResult : MatchResult()

  enum class Status {
    LOAD_MATCH_IN_FLIGHT, LOAD_MATCH_FAILURE, LOAD_MATCH_SUCCESS
  }
}
