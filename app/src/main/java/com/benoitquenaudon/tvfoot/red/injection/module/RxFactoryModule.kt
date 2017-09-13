package com.benoitquenaudon.tvfoot.red.injection.module

import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchIntent
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchViewState
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesIntent
import com.benoitquenaudon.tvfoot.red.app.domain.matches.MatchesViewState
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
