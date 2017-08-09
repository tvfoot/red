package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.source.BaseMatchRepository
import com.benoitquenaudon.tvfoot.red.testutil.Fixture
import com.benoitquenaudon.tvfoot.red.testutil.InjectionContainer
import io.reactivex.Single
import javax.inject.Inject

class FakeMatchRepository : BaseMatchRepository {
  @Inject lateinit var fixture: Fixture

  init {
    InjectionContainer.testComponentInstance.inject(this)
  }

  override fun loadMatch(matchId: String): Single<Match> = Single.just(fixture.anyMatch())
}