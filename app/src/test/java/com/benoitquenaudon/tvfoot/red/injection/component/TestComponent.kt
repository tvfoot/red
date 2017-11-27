package com.benoitquenaudon.tvfoot.red.injection.component

import com.benoitquenaudon.tvfoot.red.app.data.source.FakeMatchRepository
import com.benoitquenaudon.tvfoot.red.app.data.source.FakeMatchesRepository
import com.benoitquenaudon.tvfoot.red.app.data.source.FakePreferenceRepository
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchStateBinderTest
import com.benoitquenaudon.tvfoot.red.injection.module.FakeImplementationModule
import com.benoitquenaudon.tvfoot.red.injection.module.NetworkModule
import com.benoitquenaudon.tvfoot.red.injection.module.ServiceMockModule
import com.benoitquenaudon.tvfoot.red.injection.module.TestSchedulerModule
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
  fun inject(fakeMatchesRepository: FakeMatchesRepository)
  fun inject(fakeMatchRepository: FakeMatchRepository)
  fun inject(fakePreferenceRepository: FakePreferenceRepository)
}
