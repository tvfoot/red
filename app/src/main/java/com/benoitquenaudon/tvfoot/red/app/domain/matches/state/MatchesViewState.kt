package com.benoitquenaudon.tvfoot.red.app.domain.matches.state

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
    //    val filters: List<FilterRowDisplayable> = emptyList()
    val tags: List<Tag> = listOf(
        Tag("l1", "League 1"),
        Tag("l2", "League 2"),
        Tag("an", "Angleterre"),
        Tag("es", "Espagne"),
        Tag("it", "Italie"),
        Tag("al", "Allemagne"),
        Tag("po", "Portugal"),
        Tag("tu", "Turquie"),
        Tag("ch", "Champions League"),
        Tag("eu", "Europa League"),
        Tag("cl", "Coupe de la Ligue"),
        Tag("cm", "Coupe du monde"),
        Tag("fo", "Football Feminin"),
        Tag("am", "Amical")
    ),
    var activeFilterIds: Set<String> = emptySet()
) : MviViewState {
  fun matchesItemDisplayables(hasMore: Boolean): List<MatchesItemDisplayable> {
    val headers = ArrayList<String>()
    val items = ArrayList<MatchesItemDisplayable>()
    for (match in matches) {
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
