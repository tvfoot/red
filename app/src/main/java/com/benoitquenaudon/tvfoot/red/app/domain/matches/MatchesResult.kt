package com.benoitquenaudon.tvfoot.red.app.domain.matches

import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.mvi.MviResult
import com.benoitquenaudon.tvfoot.red.util.MatchId
import com.benoitquenaudon.tvfoot.red.util.WillBeNotified

sealed class MatchesResult : MviResult {
  sealed class RefreshResult : MatchesResult() {
    data class Success(
        val matches: List<Match>,
        val willBeNotifiedPairs: Map<MatchId, WillBeNotified>
    ) : RefreshResult()

    data class Failure(val throwable: Throwable) : RefreshResult()

    object InFlight : RefreshResult()
  }

  sealed class LoadNextPageResult : MatchesResult() {
    data class Success(
        val pageIndex: Int,
        val matches: List<Match>,
        val willBeNotifiedPairs: Map<MatchId, WillBeNotified>
    ) : LoadNextPageResult()

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
