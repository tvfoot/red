package com.benoitquenaudon.tvfoot.red.app.domain.matches.state

sealed class MatchesIntent {
  object InitialIntent : MatchesIntent()

  object RefreshIntent : MatchesIntent()

  object GetLastState : MatchesIntent()

  data class LoadNextPageIntent(val pageIndex: Int) : MatchesIntent()
}
