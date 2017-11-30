package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.api.TvfootService
import com.benoitquenaudon.tvfoot.red.app.data.entity.Team
import io.reactivex.Single
import javax.inject.Inject

class TeamRepository @Inject constructor(
    private val tvfootService: TvfootService
) : BaseTeamRepository {
  override fun findTeams(input: String): Single<List<Team>> = tvfootService.findTeams(input)
}
