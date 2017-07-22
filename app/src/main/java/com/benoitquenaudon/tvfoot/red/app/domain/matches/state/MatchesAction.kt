package com.benoitquenaudon.tvfoot.red.app.domain.matches.state

sealed class MatchesAction {
  object GetLastStateAction : MatchesAction()

  object RefreshAction : MatchesAction()

  data class LoadNextPageAction(val pageIndex: Int) : MatchesAction()
}