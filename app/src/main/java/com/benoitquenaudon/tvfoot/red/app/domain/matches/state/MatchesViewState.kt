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
        Tag("L1", "League 1"),
        Tag("L2", "League 2"),
        Tag("ANG", "Angleterre"),
        Tag("ESP", "Espagne"),
        Tag("ITA", "Italie"),
        Tag("ALL", "Allemagne"),
        Tag("POR", "Portugal"),
        Tag("TUR", "Turquie"),
        Tag("C1", "Champions League"),
        Tag("C3", "Europa League"),
        Tag("CDL", "Coupe de la Ligue"),
        Tag("CM", "Coupe du monde"),
        Tag("WOM", "Football Feminin"),
        Tag("AMI", "Amical")
    ),
    var activeFilterIds: Set<String> = emptySet()
) : MviViewState {
  fun matchesItemDisplayables(hasMore: Boolean,
      activeFilterIds: Set<String>): List<MatchesItemDisplayable> {
    val headers = ArrayList<String>()
    val items = ArrayList<MatchesItemDisplayable>()

    for (match in matches) {
      // TODO(benoit) need to manage targets...
//      if (activeFilterIds.isNotEmpty() && !activeFilterIds.contains(match.competitionCode)) continue

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
