package com.benoitquenaudon.tvfoot.red.app.domain.matches.state

import com.benoitquenaudon.tvfoot.red.app.common.LceStatus
import com.benoitquenaudon.tvfoot.red.app.common.LceStatus.FAILURE
import com.benoitquenaudon.tvfoot.red.app.common.LceStatus.IN_FLIGHT
import com.benoitquenaudon.tvfoot.red.app.common.LceStatus.SUCCESS
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.mvi.MviResult

sealed class MatchesResult : MviResult {
  @Suppress("DataClassPrivateConstructor")
  data class RefreshResult private constructor(
      val status: LceStatus,
      val matches: List<Match>?,
      val throwable: Throwable?
  ) : MatchesResult() {
    companion object Factory {
      fun success(matches: List<Match>): RefreshResult {
        return RefreshResult(SUCCESS, matches, null)
      }

      fun failure(throwable: Throwable): RefreshResult {
        return RefreshResult(FAILURE, null, throwable)
      }

      fun inFlight(): RefreshResult {
        return RefreshResult(IN_FLIGHT, null, null)
      }
    }
  }

  object GetLastStateResult : MatchesResult()

  @Suppress("DataClassPrivateConstructor")
  data class LoadNextPageResult private constructor(
      val status: LceStatus,
      val matches: List<Match>?,
      val error: Throwable?,
      val pageIndex: Int
  ) : MatchesResult() {
    companion object Factory {
      fun success(pageIndex: Int, matches: List<Match>): LoadNextPageResult {
        return LoadNextPageResult(SUCCESS, matches, null, pageIndex)
      }

      fun failure(throwable: Throwable): LoadNextPageResult {
        return LoadNextPageResult(FAILURE, null, throwable, -1)
      }

      fun inFlight(): LoadNextPageResult {
        return LoadNextPageResult(IN_FLIGHT, null, null, -1)
      }
    }
  }

  object ClearFiltersResult : MatchesResult()

  data class ToggleFilterResult(val tagName: String) : MatchesResult()

  @Suppress("DataClassPrivateConstructor")
  data class LoadTagsResult private constructor(
      val status: LceStatus,
      val tags: List<Tag>?,
      val error: Throwable?
  ) : MatchesResult() {
    companion object Factory {
      fun success(tags: List<Tag>): LoadTagsResult {
        return LoadTagsResult(SUCCESS, tags, null)
      }

      fun failure(throwable: Throwable): LoadTagsResult {
        return LoadTagsResult(FAILURE, null, throwable)
      }

      fun inFlight(): LoadTagsResult {
        return LoadTagsResult(IN_FLIGHT, null, null)
      }
    }
  }
}
