package com.benoitquenaudon.tvfoot.red.app.domain.matches

import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.ClearFiltersResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.ClearSearchInputResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.LoadTagsResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.SearchInputResult.ClearSearchResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.SearchInputResult.SearchTeamResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.SearchedTeamSelectedResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.SearchedTeamSelectedResult.TeamSearchFailure
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.SearchedTeamSelectedResult.TeamSearchInFlight
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.SearchedTeamSelectedResult.TeamSearchSuccess
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.ToggleFilterBroadcasterResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.ToggleFilterCompetitionResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.FilterResult.ToggleFilterTeamResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.LoadNextPageResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.RefreshNotificationStatusResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesResult.RefreshResult
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.TeamSearchResultDisplayable
import com.benoitquenaudon.tvfoot.red.util.TagName
import com.benoitquenaudon.tvfoot.red.util.TagTargets
import io.reactivex.functions.BiFunction

object MatchesViewStateMachine : BiFunction<MatchesViewState, MatchesResult, MatchesViewState> {
  override fun apply(previousState: MatchesViewState, result: MatchesResult): MatchesViewState {
    return when (result) {
      is RefreshResult -> {
        when (result) {
          RefreshResult.InFlight ->
            previousState.copy(refreshLoading = true, error = null)
          is RefreshResult.Failure ->
            previousState.copy(refreshLoading = false, error = result.throwable)
          is RefreshResult.Success -> {
            val matches = result.matches
            val willBeNotifiedPairs = result.willBeNotifiedPairs

            previousState.copy(
                refreshLoading = false,
                error = null,
                hasMore = !matches.isEmpty(),
                currentPage = 0,
                matches = MatchRowDisplayable.fromMatches(matches, willBeNotifiedPairs))
          }
        }
      }
      is LoadNextPageResult -> {
        when (result) {
          LoadNextPageResult.InFlight ->
            previousState.copy(nextPageLoading = true, error = null)
          is LoadNextPageResult.Failure ->
            previousState.copy(nextPageLoading = false, error = result.throwable)
          is LoadNextPageResult.Success -> {
            val newMatches =
                MatchRowDisplayable.fromMatches(result.matches, result.willBeNotifiedPairs)

            val matches = (previousState.matches + newMatches).distinct().sorted()

            previousState.copy(
                nextPageLoading = false,
                error = null,
                matches = matches,
                currentPage = result.pageIndex,
                hasMore = !newMatches.isEmpty())
          }
        }
      }
      ClearFiltersResult ->
        previousState.copy(
            filteredBroadcasters = emptyMap(),
            filteredCompetitions = emptyMap(),
            filteredTeams = emptyList()
        )
      is ToggleFilterCompetitionResult -> {
        previousState.filteredCompetitions.toMutableMap().let {
          if (it.keys.contains(result.tagName)) {
            it.remove(result.tagName)
          } else {
            it[result.tagName] = previousState.tags.first { it.name == result.tagName }.targets
          }
          previousState.copy(filteredCompetitions = it, hasMore = true)
        }
      }
      is ToggleFilterBroadcasterResult -> {
        previousState.filteredBroadcasters.toMutableMap().let {
          if (it.keys.contains(result.tagName)) {
            it.remove(result.tagName)
          } else {
            it[result.tagName] = previousState.tags.first { it.name == result.tagName }.targets
          }
          previousState.copy(filteredBroadcasters = it, hasMore = true)
        }
      }
      is ToggleFilterTeamResult -> {
        previousState.filteredTeams.toMutableList().let { filteredTeams ->
          if (filteredTeams.contains(result.teamCode)) {
            filteredTeams.remove(result.teamCode)
          } else {
            filteredTeams.add(result.teamCode)
          }
          previousState.copy(filteredTeams = filteredTeams)
        }
      }
      is LoadTagsResult -> {
        when (result) {
          LoadTagsResult.InFlight ->
            previousState.copy(tagsLoading = true, tagsError = null)
          is LoadTagsResult.Failure ->
            previousState.copy(tagsLoading = false, tagsError = result.throwable)
          is LoadTagsResult.Success -> {
            val displayedTags = result.tags.filter(Tag::display)
            val filteredBroadcasters: Map<TagName, TagTargets> = displayedTags
                .filter(Tag::isBroadcast)
                .filter { it.name in result.filteredBroadcasterNames }
                .map { it.name to it.targets }
                .toMap()
            val filteredCompetitions: Map<TagName, TagTargets> = displayedTags
                .filter(Tag::isCompetition)
                .filter { it.name in result.filteredCompetitionNames }
                .map { it.name to it.targets }
                .toMap()

            previousState.copy(
                tagsLoading = false,
                tags = displayedTags,
                filteredBroadcasters = filteredBroadcasters,
                filteredCompetitions = filteredCompetitions,
                filteredTeams = result.filteredTeamNames,
                teams = result.teams
            )
          }
        }
      }
      is SearchTeamResult ->
        when (result) {
          is SearchTeamResult.InFlight ->
            previousState.copy(
                searchingTeam = true,
                searchInput = result.searchedInput)
          is SearchTeamResult.Failure ->
            previousState.copy(searchingTeam = false, error = result.throwable)
          is SearchTeamResult.Success ->
            previousState.copy(
                searchingTeam = false,
                searchedTeams = result.teams.map { TeamSearchResultDisplayable(it) },
                searchInput = result.searchedInput
            )
        }
      ClearSearchResult ->
        previousState.copy(
            searchingTeam = false,
            searchedTeams = emptyList())
      ClearSearchInputResult -> previousState.copy()
      is SearchedTeamSelectedResult -> when (result) {
        is TeamSearchFailure ->
          previousState.copy(error = result.throwable, teamMatchesLoading = false)
        is TeamSearchSuccess -> {
          val newMatches =
              MatchRowDisplayable.fromMatches(result.matches, result.willBeNotifiedPairs)

          val matches = (previousState.matches + newMatches).distinct().sorted()

          previousState.copy(
              teamMatchesLoading = false,
              matches = matches,
              error = null
          )
        }
        is TeamSearchInFlight ->
          previousState.copy(
              teams = listOf(result.team) + previousState.teams,
              filteredTeams = previousState.filteredTeams + result.team.code,
              searchedTeams = emptyList(),
              searchInput = "",
              teamMatchesLoading = true
          )
      }
      is RefreshNotificationStatusResult -> {
        val newMatches = previousState.matches.map { match ->
          if (match.matchId == result.matchId) {
            match.copy(willBeNotified = result.willBeNotified)
          } else {
            match
          }
        }
        previousState.copy(matches = newMatches)
      }
    }
  }
}