package io.oldering.tvfoot.red.match;

import android.app.Activity;
import android.support.annotation.VisibleForTesting;
import io.oldering.tvfoot.red.di.ActivityScope;
import io.oldering.tvfoot.red.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Observable;
import javax.inject.Inject;

@ActivityScope class MatchBinder {
  private final MatchActivity activity;
  private final MatchInteractor interactor;
  private final BaseSchedulerProvider schedulerProvider;

  @Inject MatchBinder(Activity activity, MatchInteractor interactor,
      BaseSchedulerProvider schedulerProvider) {
    this.activity = (MatchActivity) activity;
    this.interactor = interactor;
    this.schedulerProvider = schedulerProvider;
  }

  @VisibleForTesting Observable<MatchIntent> intent() {
    return activity.loadMatchIntent().subscribeOn(schedulerProvider.ui());
  }

  @VisibleForTesting Observable<MatchViewState> model(Observable<MatchIntent> intents) {
    return intents.flatMap(intent -> {
      if (intent instanceof MatchIntent.LoadMatch) {
        return interactor.loadMatch().subscribeOn(schedulerProvider.io());
      }
      throw new IllegalArgumentException("I don't know how to deal with this intent " + intent);
    }).subscribeOn(schedulerProvider.computation());
  }

  @VisibleForTesting void view(Observable<MatchViewState> states) {
    states.observeOn(schedulerProvider.ui()).subscribe(activity::render);
  }

  void bind() {
    this.view(this.model(this.intent()));
  }
}
