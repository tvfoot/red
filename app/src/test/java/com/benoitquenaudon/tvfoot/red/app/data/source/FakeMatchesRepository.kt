package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.source.BaseMatchesRepository
import com.benoitquenaudon.tvfoot.red.testutil.Fixture
import com.benoitquenaudon.tvfoot.red.testutil.InjectionContainer
import io.reactivex.Single
import javax.inject.Inject

class FakeMatchesRepository : BaseMatchesRepository {
  @Inject lateinit var fixture: Fixture

  init {
    InjectionContainer.testComponentInstance.inject(this)
  }

  override fun loadPage(pageIndex: Int): Single<List<Match>> =
      when (pageIndex) {
        0 -> Single.just(fixture.matchesListA())
        1 -> Single.just(fixture.matchesListB())
        else -> throw NotImplementedError("need to provide other data")
      }
}