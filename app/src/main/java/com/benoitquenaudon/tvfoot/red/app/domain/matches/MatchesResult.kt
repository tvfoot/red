package com.benoitquenaudon.tvfoot.red.app.domain.matches

import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.mvi.MviResult

sealed class MatchesResult : MviResult {
  sealed class RefreshResult : MatchesResult() {
    data class Success(val matches: List<Match>) : RefreshResult()

    data class Failure(val throwable: Throwable) : RefreshResult()

    object InFlight : RefreshResult()
  }

  object GetLastStateResult : MatchesResult()

  sealed class LoadNextPageResult : MatchesResult() {
    data class Success(val pageIndex: Int, val matches: List<Match>) : LoadNextPageResult()

    data class Failure(val throwable: Throwable) : LoadNextPageResult()

    object InFlight : LoadNextPageResult()
  }

  object ClearFiltersResult : MatchesResult()

  data class ToggleFilterResult(val tagName: String) : MatchesResult()

  sealed class LoadTagsResult : MatchesResult() {
    data class Success(val tags: List<Tag>) : LoadTagsResult()

    data class Failure(val throwable: Throwable) : LoadTagsResult()

    object InFlight : LoadTagsResult()
  }
}
