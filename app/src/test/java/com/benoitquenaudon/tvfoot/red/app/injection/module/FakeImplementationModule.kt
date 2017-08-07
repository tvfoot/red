package com.benoitquenaudon.tvfoot.red.app.injection.module

import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.BaseMatchesRepository
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.FakeMatchesRepository
import dagger.Binds
import dagger.Module

@Module
abstract class FakeImplementationModule {
  @Binds
  abstract fun provideMatchesRepository(
      matchesRepository: FakeMatchesRepository): BaseMatchesRepository
}