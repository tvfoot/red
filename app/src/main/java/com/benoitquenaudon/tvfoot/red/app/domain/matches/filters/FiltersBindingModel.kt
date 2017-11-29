package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.databinding.ObservableBoolean
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesViewState
import com.benoitquenaudon.tvfoot.red.injection.scope.FragmentScope
import javax.inject.Inject

@FragmentScope
class FiltersBindingModel @Inject constructor(private val adapter: FiltersAdapter) {

  val loadingTags: ObservableBoolean = ObservableBoolean(true)
  private var filteredTags: Set<String> = emptySet()

  val hasFilters: ObservableBoolean = ObservableBoolean(false)

  fun updateFromState(state: MatchesViewState) {
    filteredTags = state.filteredTags.keys
    loadingTags.set(state.tagsLoading)
    hasFilters.set(filteredTags.isNotEmpty())

    val tagFilters = state.tags
        .filter { it.type == "competition" }
        .map {
          FiltersCompetitionDisplayable(
              code = it.name,
              label = it.desc,
              filtered = filteredTags.contains(it.name)) as FiltersItemDisplayable
        }
    val teamFilters = listOf(FiltersTeamSearchDisplayable)

    val filters = if (tagFilters.isEmpty()) emptyList() else teamFilters + tagFilters

    adapter.setFiltersItems(filters)
  }
}