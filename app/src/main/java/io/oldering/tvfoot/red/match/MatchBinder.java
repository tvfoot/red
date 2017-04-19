package io.oldering.tvfoot.red.match;

import android.support.annotation.VisibleForTesting;
import io.oldering.tvfoot.red.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Observable;
import javax.inject.Inject;
import timber.log.Timber;

class MatchBinder {
  private final MatchActivity activity;
  private final MatchInteractor interactor;
  private final BaseSchedulerProvider schedulerProvider;

  @Inject MatchBinder(MatchActivity activity, MatchInteractor interactor,
      BaseSchedulerProvider schedulerProvider) {
    this.activity = activity;
    this.interactor = interactor;
    this.schedulerProvider = schedulerProvider;
  }

  @VisibleForTesting Observable<MatchIntent> intent() {
    return activity.loadMatchIntent()
        .subscribeOn(schedulerProvider.ui())
        .doOnNext(intent -> Timber.d("Binder: Intent: %s", intent));
  }

  @VisibleForTesting Observable<MatchViewState> model(Observable<MatchIntent> intents) {
    return intents.flatMap(intent -> {
      if (intent instanceof MatchIntent.LoadMatch) {
        return interactor.loadMatch(((MatchIntent.LoadMatch) intent).matchId())
            .subscribeOn(schedulerProvider.io());
      }
      throw new IllegalArgumentException("I don't know how to deal with this intents " + intent);
    })
        .subscribeOn(schedulerProvider.computation())
        .doOnNext(state -> Timber.d("Binder: State: %s", state));
  }

  @VisibleForTesting void view(Observable<MatchViewState> states) {
    states.observeOn(schedulerProvider.ui()).subscribe(activity::render);
  }

  void bind() {
    this.view(this.model(this.intent()));
  }
}
