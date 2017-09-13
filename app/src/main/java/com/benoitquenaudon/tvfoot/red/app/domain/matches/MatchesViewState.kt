package com.benoitquenaudon.tvfoot.red.app.domain.matches

import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.HeaderRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.LoadingRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchesItemDisplayable
import com.benoitquenaudon.tvfoot.red.app.mvi.MviViewState
import java.util.ArrayList

data class MatchesViewState(
    val matches: List<MatchRowDisplayable> = emptyList(),
    val error: Throwable? = null,
    val nextPageLoading: Boolean = false,
    val refreshLoading: Boolean = false,
    val currentPage: Int = 0,
    val hasMore: Boolean = true,
    val tagsLoading: Boolean = false,
    val tagsError: Throwable? = null,
    val tags: List<Tag> = emptyList(),
    var filteredTags: Map<String, List<String>> = emptyMap()
) : MviViewState {
  fun matchesItemDisplayables(hasMore: Boolean, filteredTags: Map<String, List<String>>)
      : List<MatchesItemDisplayable> {
    val headers = ArrayList<String>()
    val items = ArrayList<MatchesItemDisplayable>()

    val filteredTargets = filteredTags.values.flatten().toSet()
    for (match in matches) {
      if (filteredTargets.isNotEmpty() && filteredTargets.intersect(match.tags).isEmpty()) {
        continue
      }

      if (!headers.contains(match.headerKey)) {
        headers.add(match.headerKey)
        items.add(HeaderRowDisplayable.create(match.headerKey))
      }
      items.add(match)
    }
    if (!items.isEmpty() && hasMore) {
      items.add(LoadingRowDisplayable)
    }
    return items
  }

  companion object Factory {
    fun idle(): MatchesViewState = MatchesViewState()
  }
}
