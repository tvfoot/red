package com.benoitquenaudon.tvfoot.red.app.injection.module;

import dagger.Module;
import dagger.Provides;
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchIntent;
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchViewState;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesIntent;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesViewState;
import io.reactivex.subjects.PublishSubject;

@Module public class RxFactoryModule {
  @Provides PublishSubject<MatchesIntent> provideMatchesIntentSubject() {
    return PublishSubject.create();
  }

  @Provides PublishSubject<MatchesViewState> provideMatchesViewStateSubject() {
    return PublishSubject.create();
  }

  @Provides PublishSubject<MatchIntent> provideMatchIntentSubject() {
    return PublishSubject.create();
  }

  @Provides PublishSubject<MatchViewState> provideMatchViewStateSubject() {
    return PublishSubject.create();
  }
}
