package com.benoitquenaudon.tvfoot.red.app.domain.matches

import com.benoitquenaudon.tvfoot.red.app.data.entity.FilterTeam
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.HeaderRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.LoadingRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchesItemDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.TeamSearchResultDisplayable
import com.benoitquenaudon.tvfoot.red.app.mvi.MviViewState
import com.benoitquenaudon.tvfoot.red.util.TagName
import com.benoitquenaudon.tvfoot.red.util.TagTargets
import com.benoitquenaudon.tvfoot.red.util.TeamCode
import java.util.ArrayList

data class MatchesViewState(
    val matches: List<MatchRowDisplayable> = emptyList(),
    val error: Throwable? = null,
    val nextPageLoading: Boolean = false,
    val refreshLoading: Boolean = false,
    val currentPage: Int = 0,
    val hasMore: Boolean = true,
    val tagsLoading: Boolean = false,
    val tagsError: Throwable? = null,
    val tags: List<Tag> = emptyList(),
    var filteredTags: Map<TagName, TagTargets> = emptyMap(),
    val searchInput: String = "",
    val searchingTeam: Boolean = false,
    val searchedTeams: List<TeamSearchResultDisplayable> = emptyList(),
    val teams: List<FilterTeam> = emptyList(),
    val filteredTeams: List<TeamCode> = emptyList()
) : MviViewState {
  fun matchesItemDisplayables(
      hasMore: Boolean,
      filteredTags: Map<String, List<String>>,
      filteredTeams: List<TeamCode>
  ): List<MatchesItemDisplayable> {

    val headers = ArrayList<String>()
    val items = ArrayList<MatchesItemDisplayable>()

    val filteredTargets = filteredTags.values.flatten().toSet()
    val filteredTeamsCodes = filteredTeams.toSet()
    for (match in matches) {
      if (filteredTargets.isNotEmpty() && filteredTargets.intersect(match.tags).isEmpty()) {
        continue
      }
      if (filteredTeamsCodes.isNotEmpty() &&
          filteredTeamsCodes.intersect(
              setOf(match.homeTeam.code, match.awayTeam.code)).isEmpty()) {
        continue
      }

      if (!headers.contains(match.headerKey)) {
        headers.add(match.headerKey)
        items.add(HeaderRowDisplayable.create(match.headerKey))
      }
      items.add(match)
    }
    if (!items.isEmpty() && hasMore) {
      items.add(LoadingRowDisplayable)
    }
    return items
  }

  companion object {
    fun idle(): MatchesViewState = MatchesViewState()
  }
}
