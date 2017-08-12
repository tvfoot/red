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
        Tag("L1", "Ligue 1", "competition", listOf("l1")),
        Tag("L2", "Ligue 2", "competition", listOf("l2")),
        Tag("ANG", "Angleterre", "competition", listOf("_en", "bpl", "fac", "flc", "facs", "sbc")),
        Tag("ESP", "Espagne", "competition", listOf("_es", "liga", "cdr,sde")),
        Tag("ITA", "Italie", "competition", listOf("_it", "sa", "timc", "sdi")),
        Tag("ALL", "Allemagne", "competition", listOf("_de", "bun", "dfbp", "dfls")),
        Tag("POR", "Portugal", "competition", listOf("_po", "plp", "tdp", "tdl", "stco")),
        Tag("TUR", "Turquie", "competition", listOf("_tu", "tsl")),
        Tag("C1", "Champions League", "competition", listOf("ucl")),
        Tag("C3", "Europa League", "competition", listOf("uel")),
        Tag("CDL", "Coupe de la Ligue", "competition", listOf("cdl")),
        Tag("CM", "Coupe du monde", "competition", listOf("cm")),
        Tag("WOM", "Football Féminin", "competition", listOf("f")),
        Tag("AMI", "Amical", "competition", listOf("ami"))
    ),
    var filteredTags: Map<String, List<String>> = emptyMap<String, List<String>>()
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
