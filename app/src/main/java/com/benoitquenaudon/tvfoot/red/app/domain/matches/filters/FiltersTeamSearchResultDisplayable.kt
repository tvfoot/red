package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import com.benoitquenaudon.tvfoot.red.app.data.entity.Team

data class FiltersTeamSearchResultDisplayable(
    val code: String,
    val name: String,
    val fullName: String,
    val type: String
) : FiltersItemDisplayable {
  override fun isSameAs(other: FiltersItemDisplayable): Boolean {
    return if (other is FiltersTeamSearchResultDisplayable) {
      this.code == other.code
    } else {
      false
    }
  }

  companion object Factory {
    operator fun invoke(team: Team): FiltersTeamSearchResultDisplayable {
      // So, we need those but the server's API is funky so we check NPEs
      return FiltersTeamSearchResultDisplayable(
          code = checkNotNull(team.code) { "Team code is null" },
          name = checkNotNull(team.name) { "Team name is null" },
          fullName = checkNotNull(team.fullname) { "Team fullname is null" },
          type = checkNotNull(team.type) { "Team type is null" }
      )
    }
  }
}