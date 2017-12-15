package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.testutil.Fixture
import com.benoitquenaudon.tvfoot.red.testutil.InjectionContainer
import com.benoitquenaudon.tvfoot.red.util.MatchId
import com.benoitquenaudon.tvfoot.red.util.TeamCode
import com.benoitquenaudon.tvfoot.red.util.WillBeNotified
import io.reactivex.Single
import javax.inject.Inject

class FakeMatchesRepository : BaseMatchesRepository {

  @Inject lateinit var fixture: Fixture

  init {
    InjectionContainer.testComponentInstance.inject(this)
  }

  override fun loadPage(pageIndex: Int): Single<List<Match>> {
    return when (pageIndex) {
      0 -> Single.just(fixture.matchesListA())
      1 -> Single.just(fixture.matchesListB())
      else -> throw NotImplementedError("need to provide other data")
    }
  }

  override fun loadTags(): Single<List<Tag>> = Single.just(fixture.tags())

  override fun searchTeamMatches(vararg codes: TeamCode): Single<List<Match>> =
      Single.just(fixture.anyMatches())

  override fun loadPageWithNotificationStatus(
      pageIndex: Int
  ): Single<Pair<List<Match>, Map<MatchId, WillBeNotified>>> {
    return when (pageIndex) {
      0 -> Single.just(Pair(fixture.matchesListA(), emptyMap()))
      1 -> Single.just(Pair(fixture.matchesListB(), emptyMap()))
      else -> throw NotImplementedError("need to provide other data")
    }
  }

  override fun searchTeamMatchesWithNotificationStatus(
      vararg codes: TeamCode
  ): Single<Pair<List<Match>, Map<MatchId, WillBeNotified>>> {
    return Single.just(Pair(fixture.anyMatches(), emptyMap()))
  }
}