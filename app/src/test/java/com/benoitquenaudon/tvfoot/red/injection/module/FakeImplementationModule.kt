package com.benoitquenaudon.tvfoot.red.injection.module

import com.benoitquenaudon.tvfoot.red.app.data.source.BaseMatchRepository
import com.benoitquenaudon.tvfoot.red.app.data.source.BaseMatchesRepository
import com.benoitquenaudon.tvfoot.red.app.data.source.FakeMatchRepository
import com.benoitquenaudon.tvfoot.red.app.data.source.FakeMatchesRepository
import dagger.Binds
import dagger.Module

@Module
abstract class FakeImplementationModule {
  @Binds
  abstract fun provideMatchesRepository(
      matchesRepository: FakeMatchesRepository): BaseMatchesRepository

  @Binds
  abstract fun provideMatchRepository(matchRepository: FakeMatchRepository): BaseMatchRepository
}