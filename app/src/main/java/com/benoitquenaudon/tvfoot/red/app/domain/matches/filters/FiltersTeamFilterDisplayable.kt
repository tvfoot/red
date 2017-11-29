package com.benoitquenaudon.tvfoot.red.app.domain.matches.filters

import com.benoitquenaudon.tvfoot.red.app.data.entity.Team

data class FiltersTeamFilterDisplayable(
    val code: String,
    val name: String,
    val fullName: String,
    val type: String
) : FiltersItemDisplayable {
  override fun isSameAs(other: FiltersItemDisplayable): Boolean {
    return if (other is FiltersTeamFilterDisplayable) {
      this.code == other.code
    } else {
      false
    }
  }

  companion object Factory {
    operator fun invoke(team: Team): FiltersTeamFilterDisplayable {
      // So, we need those but the server's API is funky so we check NPEs
      return FiltersTeamFilterDisplayable(
          code = checkNotNull(team.code) { "Team code is null" },
          name = checkNotNull(team.name) { "Team name is null" },
          fullName = checkNotNull(team.fullname) { "Team fullname is null" },
          type = checkNotNull(team.type) { "Team type is null" }
      )
    }
  }
}