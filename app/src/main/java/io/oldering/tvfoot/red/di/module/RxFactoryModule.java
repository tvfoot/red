package io.oldering.tvfoot.red.di.module;

import dagger.Module;
import dagger.Provides;
import io.oldering.tvfoot.red.match.state.MatchIntent;
import io.oldering.tvfoot.red.match.state.MatchViewState;
import io.oldering.tvfoot.red.matches.state.MatchesIntent;
import io.oldering.tvfoot.red.matches.state.MatchesViewState;
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
