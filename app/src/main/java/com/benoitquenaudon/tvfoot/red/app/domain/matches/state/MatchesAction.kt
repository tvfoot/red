package com.benoitquenaudon.tvfoot.red.app.domain.matches.state

import com.benoitquenaudon.tvfoot.red.app.mvi.MviAction

sealed class MatchesAction : MviAction {
  object GetLastStateAction : MatchesAction()

  object RefreshAction : MatchesAction()

  data class LoadNextPageAction(val pageIndex: Int) : MatchesAction()

  data class ToggleFilterAction(val filterCode: String) : MatchesAction()

  object ClearFiltersAction : MatchesAction()
}