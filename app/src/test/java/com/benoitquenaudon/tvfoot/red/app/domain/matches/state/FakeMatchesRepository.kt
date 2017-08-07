package com.benoitquenaudon.tvfoot.red.app.domain.matches.state

import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.testutil.Fixture
import com.benoitquenaudon.tvfoot.red.testutil.InjectionContainer
import io.reactivex.Single
import javax.inject.Inject

class FakeMatchesRepository : BaseMatchesRepository {
  @Inject lateinit var fixture: Fixture

  init {
    InjectionContainer.testComponentInstance.inject(this)
  }

  override fun loadPage(pageIndex: Int): Single<List<Match>> {
    return Single.just(fixture.anyMatches())
  }

}