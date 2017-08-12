package com.benoitquenaudon.tvfoot.red.app.domain.matches.state

import com.benoitquenaudon.tvfoot.red.app.mvi.MviIntent

sealed class MatchesIntent : MviIntent {
  object InitialIntent : MatchesIntent()

  object RefreshIntent : MatchesIntent()

  object GetLastState : MatchesIntent()

  data class LoadNextPageIntent(val pageIndex: Int) : MatchesIntent()

  object ClearFilters : MatchesIntent()

  data class ToggleFilterIntent(val tagName: String) : MatchesIntent()
}
