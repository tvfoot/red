package com.benoitquenaudon.tvfoot.red.app.domain.matches

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import timber.log.Timber
import javax.inject.Inject

class MatchesBindingModel @Inject constructor(private val adapter: MatchesAdapter) {
  var refreshLoading = ObservableBoolean(false)
  var hasError = ObservableBoolean(false)
  var hasData = ObservableBoolean(false)
  var nextPageLoading = false
  var hasMore = true
  var errorMessage = ObservableField<String>()
  var currentPage = 0
    private set
  var areTagsLoaded = ObservableBoolean(false)
  var hasActiveFilters = ObservableBoolean(false)

  fun updateFromState(state: MatchesViewState) {
    currentPage = state.currentPage
    Timber.d("connard update from state %s", state.tags.isNotEmpty())
    areTagsLoaded.set(state.tags.isNotEmpty())
    hasActiveFilters.set(state.filteredTags.isNotEmpty())

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
      adapter.setMatchesItems(state.matchesItemDisplayables(hasMore, state.filteredTags))
    }
  }
}
