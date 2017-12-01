package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.databinding.ObservableBoolean
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.data.entity.FilterTeam
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesViewState
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FilterHeaderDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FilterSearchLoadingRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FiltersAppliableItem.FiltersCompetitionDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FiltersAppliableItem.FiltersTeamDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.TeamSearchInputDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.TeamSearchResultDisplayable
import com.benoitquenaudon.tvfoot.red.injection.scope.FragmentScope
import com.benoitquenaudon.tvfoot.red.util.TeamCode
import javax.inject.Inject

@FragmentScope
class FiltersBindingModel @Inject constructor(private val adapter: FiltersAdapter) {

  val loadingTags: ObservableBoolean = ObservableBoolean(true)
  private var filteredTags: Set<String> = emptySet()

  val hasFilters: ObservableBoolean = ObservableBoolean(false)

  fun updateFromState(state: MatchesViewState) {
    filteredTags = state.filteredTags.keys
    loadingTags.set(state.tagsLoading)
    hasFilters.set(filteredTags.isNotEmpty() || state.filteredTeams.isNotEmpty())

    adapter.setFiltersItems(
        buildFilterList(
            state.tags,
            state.searchedTeams,
            state.searchingTeam,
            state.teams,
            state.filteredTeams
        )
    )
  }

  private fun buildFilterList(
      tags: List<Tag>,
      searchedTeams: List<TeamSearchResultDisplayable>,
      searchingTeam: Boolean,
      teams: List<FilterTeam>,
      filteredTeams: List<TeamCode>
  ): List<FiltersItemDisplayable> {
    val tagFilters = tags
        .filter { it.type == "competition" }
        .map {
          FiltersCompetitionDisplayable(
              code = it.name,
              label = it.desc,
              filtered = filteredTags.contains(it.name))
        }
    val teamFilters = teams
        .map {
          FiltersTeamDisplayable(
              code = it.code,
              name = it.name,
              type = it.type,
              country = it.country,
              filtered = filteredTeams.contains(it.code)
          )
        }

    val teamSearchDisplayables = if (searchingTeam) {
      listOf(FilterSearchLoadingRowDisplayable)
    } else {
      searchedTeams.filter { searched -> teams.none { it.code == searched.code } }
    }

    // so ugly TODO(benoit) refactor this shit
    return listOf(TeamSearchInputDisplayable) +
        teamSearchDisplayables +
        if (teamFilters.isEmpty()) {
          emptyList()
        } else {
          listOf(FilterHeaderDisplayable(R.string.teams))
        } +
        teamFilters +
        if (tagFilters.isEmpty()) {
          emptyList()
        } else {
          listOf(FilterHeaderDisplayable(R.string.competitions))
        } +
        tagFilters
  }
}