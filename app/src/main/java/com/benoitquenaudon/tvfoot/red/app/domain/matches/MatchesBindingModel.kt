package com.benoitquenaudon.tvfoot.red.app.domain.matches

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import javax.inject.Inject

class MatchesBindingModel @Inject constructor(private val adapter: MatchesAdapter) {
  var refreshLoading = ObservableBoolean(false)
  var hasError = ObservableBoolean(false)
  var hasData = ObservableBoolean(false)
  var nextPageLoading = false
  var hasMore = true
  var loadingSpecificMatches = false
  var errorMessage = ObservableField<String>()
  var currentPage = 0
    private set
  var areTagsLoaded = ObservableBoolean(false)
  var hasActiveFilters = ObservableBoolean(false)

  fun updateFromState(state: MatchesViewState) {
    currentPage = state.currentPage
    areTagsLoaded.set(state.tags.isNotEmpty())
    hasActiveFilters.set(
        state.filteredBroadcasters.isNotEmpty() ||
        state.filteredCompetitions.isNotEmpty() ||
            state.filteredTeams.isNotEmpty())

    nextPageLoading = state.nextPageLoading
    hasError.set(state.error != null)
    hasMore = state.hasMore
    loadingSpecificMatches = state.teamMatchesLoading

    if (hasError.get()) {
      val error = checkNotNull(state.error) { "state error is null" }
      errorMessage.set(error.toString())
    }

    val matchesDisplayables = state.matchesItemDisplayables(
        nextPageLoading = state.nextPageLoading,
        loadingSpecificMatches = loadingSpecificMatches,
        filteredBroadcasters = state.filteredBroadcasters,
        filteredCompetitions = state.filteredCompetitions,
        filteredTeams = state.filteredTeams
    )
    refreshLoading.set(state.refreshLoading ||
        (matchesDisplayables.isEmpty() && state.teamMatchesLoading))
    hasData.set(matchesDisplayables.isNotEmpty() ||
        state.refreshLoading ||
        state.nextPageLoading ||
        state.teamMatchesLoading)
    if (hasData.get()) {
      adapter.setMatchesItems(matchesDisplayables)
    }
  }
}
