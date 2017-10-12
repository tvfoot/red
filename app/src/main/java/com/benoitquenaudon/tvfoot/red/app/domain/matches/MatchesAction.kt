package com.benoitquenaudon.tvfoot.red.app.domain.matches

import com.benoitquenaudon.tvfoot.red.app.mvi.MviAction

sealed class MatchesAction : MviAction {
  object RefreshAction : MatchesAction()

  data class LoadNextPageAction(val pageIndex: Int) : MatchesAction()

  data class ToggleFilterAction(val tagName: String) : MatchesAction()

  object ClearFiltersAction : MatchesAction()

  object LoadTagsAction : MatchesAction()
}
