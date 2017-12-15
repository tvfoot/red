package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.util.TeamCode
import io.reactivex.Single

interface BaseMatchesRepository {
  fun loadPage(pageIndex: Int): Single<List<Match>>
  fun loadTags(): Single<List<Tag>>
  fun searchTeamMatches(vararg codes: TeamCode): Single<List<Match>>
}