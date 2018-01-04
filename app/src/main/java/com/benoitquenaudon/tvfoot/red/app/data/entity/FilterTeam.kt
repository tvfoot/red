package com.benoitquenaudon.tvfoot.red.app.data.entity

import com.benoitquenaudon.tvfoot.red.app.domain.matches.filters.FiltersItemDisplayable.TeamSearchResultDisplayable

data class FilterTeam(
    val code: String,
    val name: String,
    val type: String,
    val country: String
) {
  companion object {
    operator fun invoke(teamSearchResultDisplayable: TeamSearchResultDisplayable): FilterTeam {
      return FilterTeam(
          code = teamSearchResultDisplayable.code,
          type = teamSearchResultDisplayable.type,
          name = teamSearchResultDisplayable.name,
          country = teamSearchResultDisplayable.country
      )
    }
  }
}