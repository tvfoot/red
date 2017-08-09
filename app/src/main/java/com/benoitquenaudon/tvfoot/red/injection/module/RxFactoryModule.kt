package com.benoitquenaudon.tvfoot.red.injection.module

import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchIntent
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchViewState
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesViewState
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject

@Module object RxFactoryModule {
  @JvmStatic @Provides fun provideMatchesIntentSubject(): PublishSubject<MatchesIntent> {
    return PublishSubject.create<MatchesIntent>()
  }

  @JvmStatic @Provides fun provideMatchesViewStateSubject(): PublishSubject<MatchesViewState> {
    return PublishSubject.create<MatchesViewState>()
  }

  @JvmStatic @Provides fun provideMatchIntentSubject(): PublishSubject<MatchIntent> {
    return PublishSubject.create<MatchIntent>()
  }

  @JvmStatic @Provides fun provideMatchViewStateSubject(): PublishSubject<MatchViewState> {
    return PublishSubject.create<MatchViewState>()
  }
}
