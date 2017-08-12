package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.api.TvfootService
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import io.reactivex.Single
import javax.inject.Inject

class MatchRepository @Inject constructor(
    private val tvfootService: TvfootService
) : BaseMatchRepository {
  override fun loadMatch(matchId: String): Single<Match> = tvfootService.getMatch(matchId)
}
