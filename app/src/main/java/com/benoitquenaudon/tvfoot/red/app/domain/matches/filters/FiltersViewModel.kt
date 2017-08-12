package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesViewState
import com.benoitquenaudon.tvfoot.red.injection.scope.FragmentScope
import javax.inject.Inject

@FragmentScope class FiltersViewModel @Inject constructor(private val adapter: FiltersAdapter) {

  var filteredTags: Set<String> = emptySet()

  val hasFilters: Boolean
    get() = filteredTags.isNotEmpty()

  fun updateFromState(state: MatchesViewState) {
    filteredTags = state.filteredTags.keys

    state.tags.map {
      FilterRowDisplayable(
          code = it.name,
          label = it.desc,
          filtered = filteredTags.contains(it.name))
    }.also {
      adapter.updateFilters(it)
    }
  }
}