package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import io.reactivex.Single

interface BaseMatchRepository {
  fun loadMatch(matchId: String): Single<Match>
}