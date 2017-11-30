package com.benoitquenaudon.tvfoot.red.app.domain.matches

import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.TeamSearchResultDisplayable
import com.benoitquenaudon.tvfoot.red.app.mvi.MviIntent
import com.benoitquenaudon.tvfoot.red.util.TeamCode

sealed class MatchesIntent : MviIntent {
  object InitialIntent : MatchesIntent()

  object RefreshIntent : MatchesIntent()

  data class LoadNextPageIntent(val pageIndex: Int) : MatchesIntent()

  sealed class FilterIntent : MatchesIntent() {
    object ClearFilters : FilterIntent()

    sealed class ToggleFilterIntent : FilterIntent() {
      data class ToggleFilterCompetitionIntent(val tagName: String) : ToggleFilterIntent()

      data class ToggleFilterTeamIntent(val teamCode: TeamCode) : ToggleFilterIntent()
    }

    object FilterInitialIntent : FilterIntent()

    data class SearchTeamIntent(val input: String) : FilterIntent()

    object ClearSearchIntent : FilterIntent()

    data class SearchedTeamSelectedIntent(val team: TeamSearchResultDisplayable) : FilterIntent()
  }
}
