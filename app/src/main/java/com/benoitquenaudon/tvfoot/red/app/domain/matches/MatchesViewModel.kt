package com.benoitquenaudon.tvfoot.red.app.domain.matches

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesViewState
import javax.inject.Inject

class MatchesViewModel @Inject constructor(private val adapter: MatchesAdapter) {
  var refreshLoading = ObservableBoolean(false)
  var hasError = ObservableBoolean(false)
  var hasData = ObservableBoolean(false)
  var nextPageLoading = false
  var hasMore = true
  var errorMessage = ObservableField<String>()
  var currentPage = 0
    private set

  fun updateFromState(state: MatchesViewState) {
    currentPage = state.currentPage

    nextPageLoading = state.nextPageLoading
    refreshLoading.set(state.refreshLoading)
    hasError.set(state.error != null)
    hasData.set(!state.matches.isEmpty())
    hasMore = state.hasMore

    if (hasError.get()) {
      val error = checkNotNull(state.error) { "state error is null" }
      errorMessage.set(error.toString())
    }
    if (hasData.get()) {
      adapter.setMatchesItems(state.matchesItemDisplayables(hasMore, state.activeFilterIds))
    }
  }
}
