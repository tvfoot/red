package com.benoitquenaudon.tvfoot.red.app.domain.matches

import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.TeamSearchResultDisplayable
import com.benoitquenaudon.tvfoot.red.app.mvi.MviIntent

sealed class MatchesIntent : MviIntent {
  object InitialIntent : MatchesIntent()

  object RefreshIntent : MatchesIntent()

  data class LoadNextPageIntent(val pageIndex: Int) : MatchesIntent()

  sealed class FilterIntent : MatchesIntent() {
    object ClearFilters : FilterIntent()

    data class ToggleFilterIntent(val tagName: String) : FilterIntent()

    object FilterInitialIntent : FilterIntent()

    data class SearchTeamIntent(val input: String) : FilterIntent()

    object ClearSearchIntent : FilterIntent()

    data class SearchedTeamSelectedIntent(val team: TeamSearchResultDisplayable) : FilterIntent()
  }
}
