package com.benoitquenaudon.tvfoot.red.app.domain.match

import com.benoitquenaudon.tvfoot.red.app.mvi.MviViewState

data class MatchViewState(
    val match: MatchDisplayable? = null,
    val error: Throwable? = null,
    val loading: Boolean,
    val shouldNotifyMatchStart: Boolean) : MviViewState {
  companion object Factory {
    fun idle(): MatchViewState = MatchViewState(shouldNotifyMatchStart = true, loading = false)
  }
}
