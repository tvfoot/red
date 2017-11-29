package com.benoitquenaudon.tvfoot.red.app.domain.matches

import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.TeamSearchResultDisplayable
import com.benoitquenaudon.tvfoot.red.app.mvi.MviAction

sealed class MatchesAction : MviAction {
  object RefreshAction : MatchesAction()

  data class LoadNextPageAction(val pageIndex: Int) : MatchesAction()

  sealed class FilterAction : MatchesAction() {
    data class ToggleFilterAction(val tagName: String) : FilterAction()

    object ClearFiltersAction : FilterAction()

    object LoadTagsAction : FilterAction()

    data class SearchTeamAction(val input: String) : FilterAction()

    object ClearSearchAction : FilterAction()

    data class SearchedTeamSelectedAction(val team: TeamSearchResultDisplayable) : FilterAction()
  }
}
