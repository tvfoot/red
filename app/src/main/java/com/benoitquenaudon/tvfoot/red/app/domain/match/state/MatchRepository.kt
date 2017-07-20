package com.benoitquenaudon.tvfoot.red.app.domain.match.state

import com.benoitquenaudon.tvfoot.red.api.TvfootService
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import io.reactivex.Single
import javax.inject.Inject

open class MatchRepository @Inject constructor(private val tvfootService: TvfootService) {
   fun loadMatch(matchId: String): Single<Match> = tvfootService.getMatch(matchId)
}
