package com.benoitquenaudon.tvfoot.red.app.domain.matches

import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.TeamSearchResultDisplayable
import com.benoitquenaudon.tvfoot.red.app.mvi.MviAction
import com.benoitquenaudon.tvfoot.red.util.TeamCode

sealed class MatchesAction : MviAction {
  object RefreshAction : MatchesAction()

  data class LoadNextPageAction(val pageIndex: Int) : MatchesAction()

  sealed class FilterAction : MatchesAction() {
    data class ToggleFilterCompetitionAction(val tagName: String) : FilterAction()

    data class ToggleFilterTeamAction(val teamCode: TeamCode) : FilterAction()

    object ClearFiltersAction : FilterAction()

    object LoadTagsAction : FilterAction()

    sealed class SearchInputAction : FilterAction() {
      data class SearchTeamAction(val input: String) : SearchInputAction()
      object ClearSearchAction : SearchInputAction()
    }

    object ClearSearchInputAction : FilterAction()

    data class SearchedTeamSelectedAction(val team: TeamSearchResultDisplayable) : FilterAction()
  }
}
