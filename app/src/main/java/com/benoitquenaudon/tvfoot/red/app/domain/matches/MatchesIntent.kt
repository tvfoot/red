package com.benoitquenaudon.tvfoot.red.app.domain.matches

import com.benoitquenaudon.tvfoot.red.app.mvi.MviIntent

sealed class MatchesIntent : MviIntent {
  object InitialIntent : MatchesIntent()

  object RefreshIntent : MatchesIntent()

  data class LoadNextPageIntent(val pageIndex: Int) : MatchesIntent()

  object ClearFilters : MatchesIntent()

  data class ToggleFilterIntent(val tagName: String) : MatchesIntent()

  object FilterInitialIntent : MatchesIntent()
}
