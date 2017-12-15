package com.benoitquenaudon.tvfoot.red.testutil

import com.benoitquenaudon.tvfoot.red.api.TvfootService
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.data.entity.Team
import com.benoitquenaudon.tvfoot.red.app.data.entity.search.Filter
import com.benoitquenaudon.tvfoot.red.app.data.entity.search.TagsFilter
import io.reactivex.Single
import retrofit2.http.Path
import retrofit2.http.Query

class FakeTvfootService(private val fixture: Fixture) : TvfootService {
  override fun findTeams(input: String): Single<List<Team>> {
    return Single.just(fixture.anyTeams())
  }

  override fun getMatches(@Query("filter") filter: Filter): Single<List<Match>> {
    if (filter.offset == 0) return Single.just(fixture.anyMatches())
    if (filter.offset > 0) return Single.just(fixture.anyNextMatches())
    throw IllegalStateException("negative offset are not supposed to be")
  }

  override fun getMatch(@Path("matchId") matchId: String): Single<Match> {
    return Single.just(fixture.anyMatch())
  }

  override fun getTags(@Query("filter") filter: TagsFilter): Single<List<Tag>> {
    return Single.just(fixture.tags())
  }
}
