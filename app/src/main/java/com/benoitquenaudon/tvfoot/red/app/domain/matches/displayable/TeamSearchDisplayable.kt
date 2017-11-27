package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable

import com.benoitquenaudon.tvfoot.red.app.data.entity.Team

data class TeamSearchDisplayable(
    val code: String,
    val name: String,
    val fullName: String,
    val type: String
) {
  companion object Factory {
    operator fun invoke(team: Team): TeamSearchDisplayable {
      // So, we need those but the server's API is funky so we check NPEs
      return TeamSearchDisplayable(
          code = checkNotNull(team.code) { "Team code is null" },
          name = checkNotNull(team.name) { "Team name is null" },
          fullName = checkNotNull(team.fullname) { "Team fullname is null" },
          type = checkNotNull(team.type) { "Team type is null" }
      )
    }
  }
}