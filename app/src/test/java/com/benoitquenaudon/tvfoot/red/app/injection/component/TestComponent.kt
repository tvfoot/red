package com.benoitquenaudon.tvfoot.red.app.injection.component

import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchRepository
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchStateBinderTest
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesRepository
import com.benoitquenaudon.tvfoot.red.app.injection.module.NetworkModule
import com.benoitquenaudon.tvfoot.red.app.injection.module.ServiceMockModule
import com.benoitquenaudon.tvfoot.red.app.injection.module.TestSchedulerModule
import com.benoitquenaudon.tvfoot.red.testutil.Fixture
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
    NetworkModule::class,
    ServiceMockModule::class,
    TestSchedulerModule::class))
interface TestComponent {
  fun fixture(): Fixture

  fun schedulerProvider(): BaseSchedulerProvider

  fun matchesRepository(): MatchesRepository

  fun matchRepository(): MatchRepository

  fun inject(matchStateBinderTest: MatchStateBinderTest)
}
