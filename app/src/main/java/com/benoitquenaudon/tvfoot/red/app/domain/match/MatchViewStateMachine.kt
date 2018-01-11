package com.benoitquenaudon.tvfoot.red.app.domain.match

import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchResult.LoadMatchResult
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchResult.NotifyMatchStartResult
import io.reactivex.functions.BiFunction

object MatchViewStateMachine : BiFunction<MatchViewState, MatchResult, MatchViewState> {
  override fun apply(previousState: MatchViewState, matchResult: MatchResult): MatchViewState {
    return when (matchResult) {
      is LoadMatchResult -> {
        when (matchResult) {
          is LoadMatchResult.InFlight ->
            previousState.copy(loading = true, error = null)
          is LoadMatchResult.Failure ->
            previousState.copy(loading = false, error = matchResult.throwable)
          is LoadMatchResult.Success -> {
            previousState.copy(
                loading = false,
                error = null,
                shouldNotifyMatchStart = matchResult.shouldNotifyMatchStart,
                match = MatchDisplayable.fromMatch(matchResult.match))
          }
        }
      }
      is NotifyMatchStartResult -> {
        previousState.copy(shouldNotifyMatchStart = matchResult.shouldNotifyMatchStart)
      }
    }
  }
}