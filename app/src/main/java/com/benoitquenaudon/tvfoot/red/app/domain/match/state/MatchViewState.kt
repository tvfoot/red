package com.benoitquenaudon.tvfoot.red.app.domain.match.state

import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchDisplayable

data class MatchViewState(
    val match: MatchDisplayable? = null,
    val error: Throwable? = null,
    val loading: Boolean,
    val shouldNotifyMatchStart: Boolean) {
  companion object {
    fun idle(): MatchViewState = MatchViewState(shouldNotifyMatchStart = true, loading = false)
  }
}
