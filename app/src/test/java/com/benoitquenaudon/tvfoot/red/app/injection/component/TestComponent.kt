package com.benoitquenaudon.tvfoot.red.app.injection.component

import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchStateBinderTest
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.FakeMatchesRepository
import com.benoitquenaudon.tvfoot.red.app.injection.module.FakeImplementationModule
import com.benoitquenaudon.tvfoot.red.app.injection.module.NetworkModule
import com.benoitquenaudon.tvfoot.red.app.injection.module.ServiceMockModule
import com.benoitquenaudon.tvfoot.red.app.injection.module.TestSchedulerModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(
    NetworkModule::class,
    ServiceMockModule::class,
    TestSchedulerModule::class,
    FakeImplementationModule::class))
interface TestComponent {
  fun inject(matchStateBinderTest: MatchStateBinderTest)
  fun inject(matchStateBinderTest: FakeMatchesRepository)
}
