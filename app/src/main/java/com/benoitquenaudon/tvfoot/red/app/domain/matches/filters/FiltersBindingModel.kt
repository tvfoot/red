package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import android.databinding.ObservableBoolean
import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.app.data.entity.FilterTeam
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesViewState
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FilterHeaderDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FilterSearchLoadingRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.FiltersAppliableItem.FiltersBroadcasterDisplayable
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
  private var filteredCompetitions: Set<String> = emptySet()
  private var filteredBroadcasters: Set<String> = emptySet()

  val hasFilters: ObservableBoolean = ObservableBoolean(false)

  fun updateFromState(state: MatchesViewState) {
    filteredCompetitions = state.filteredCompetitions.keys
    filteredBroadcasters = state.filteredBroadcasters.keys
    loadingTags.set(state.tagsLoading)
    hasFilters.set(
        filteredCompetitions.isNotEmpty() ||
            filteredBroadcasters.isNotEmpty() ||
            state.filteredTeams.isNotEmpty())

    adapter.setFiltersItems(
        buildFilterList(
            state.searchInput,
            state.tags,
            state.searchedTeams,
            state.searchingTeam,
            state.teams,
            state.filteredTeams
        )
    )
  }

  private fun buildFilterList(
      inputText: String,
      tags: List<Tag>,
      searchedTeams: List<TeamSearchResultDisplayable>,
      searchingTeam: Boolean,
      teams: List<FilterTeam>,
      filteredTeams: List<TeamCode>
  ): List<FiltersItemDisplayable> {
    val competitionFilters = tags
        .filter(Tag::isCompetition)
        .map {
          FiltersCompetitionDisplayable(
              code = it.name,
              label = it.desc,
              filtered = filteredCompetitions.contains(it.name))
        }
    val broadcasterFilters = tags
        .filter(Tag::isBroadcast)
        .map {
          FiltersBroadcasterDisplayable(
              code = it.name,
              label = it.desc,
              filtered = filteredBroadcasters.contains(it.name))
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

    // so ugly TODO(benoit) come on, refactor this shit
    return listOf(TeamSearchInputDisplayable(inputText)) +
        teamSearchDisplayables +
        if (teamFilters.isEmpty()) {
          emptyList()
        } else {
          listOf(FilterHeaderDisplayable(R.string.teams))
        } +
        teamFilters +
        if (competitionFilters.isEmpty()) {
          emptyList()
        } else {
          listOf(FilterHeaderDisplayable(R.string.competitions))
        } +
        competitionFilters +
        if (broadcasterFilters.isEmpty()) {
          emptyList()
        } else {
          listOf(FilterHeaderDisplayable(R.string.broadcasters))
        } +
        broadcasterFilters
  }
}