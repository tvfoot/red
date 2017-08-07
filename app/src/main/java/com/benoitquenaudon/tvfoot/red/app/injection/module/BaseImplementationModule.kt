package com.benoitquenaudon.tvfoot.red.app.injection.module

import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.BaseMatchesRepository
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesRepository
import dagger.Binds
import dagger.Module

@Module
abstract class BaseImplementationModule {
  @Binds
  abstract fun provideMatchesRepository(matchesRepository: MatchesRepository): BaseMatchesRepository
}