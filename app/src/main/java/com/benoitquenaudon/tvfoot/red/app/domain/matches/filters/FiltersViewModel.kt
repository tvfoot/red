package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesViewState
import com.benoitquenaudon.tvfoot.red.injection.scope.FragmentScope
import javax.inject.Inject

@FragmentScope class FiltersViewModel @Inject constructor(private val adapter: FiltersAdapter) {

  var activeFilterIds: Set<String> = emptySet()

  val hasFilters: Boolean
    get() = activeFilterIds.isNotEmpty()

  fun updateFromState(state: MatchesViewState) {
    activeFilterIds = state.activeFilterIds

    state.tags.map {
      FilterRowDisplayable(it.id, it.name, activeFilterIds.contains(it.id))
    }.also {
      adapter.updateFilters(it)
    }
  }
}