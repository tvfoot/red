package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.app.data.entity.Team
import io.reactivex.Single

interface BaseTeamRepository {
  fun findTeams(input: String): Single<List<Team>>
}