package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.app.data.entity.Team
import com.benoitquenaudon.tvfoot.red.testutil.Fixture
import com.benoitquenaudon.tvfoot.red.testutil.InjectionContainer
import io.reactivex.Single
import javax.inject.Inject

class FakeTeamRepository : BaseTeamRepository {
  @Inject lateinit var fixture: Fixture

  init {
    InjectionContainer.testComponentInstance.inject(this)
  }

  override fun findTeams(input: String): Single<List<Team>> = Single.just(fixture.anyTeams())
}