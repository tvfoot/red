package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.databinding.ObservableBoolean
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesViewState
import com.benoitquenaudon.tvfoot.red.injection.scope.FragmentScope
import javax.inject.Inject

@FragmentScope class FiltersBindingModel @Inject constructor(private val adapter: FiltersAdapter) {

  val loadingTags: ObservableBoolean = ObservableBoolean(true)
  var filteredTags: Set<String> = emptySet()

  val hasFilters: ObservableBoolean = ObservableBoolean(false)

  fun updateFromState(state: MatchesViewState) {
    filteredTags = state.filteredTags.keys
    loadingTags.set(state.tagsLoading)
    hasFilters.set(filteredTags.isNotEmpty())

    state.tags
        .filter { it.type == "competition" }
        .map {
          FilterRowDisplayable(
              code = it.name,
              label = it.desc,
              filtered = filteredTags.contains(it.name))
        }.also {
      adapter.updateFilters(it)
    }
  }
}