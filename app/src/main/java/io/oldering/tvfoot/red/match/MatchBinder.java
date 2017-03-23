package io.oldering.tvfoot.red.match;

import io.oldering.tvfoot.red.di.ActivityScope;

@ActivityScope class MatchBinder {
  //private final MatchActivity activity;
  //private final MatchInteractor interactor;
  //private final BaseSchedulerProvider schedulerProvider;
  //
  //@Inject MatchBinder(Activity activity, MatchesInteractor interactor,
  //    BaseSchedulerProvider schedulerProvider) {
  //  this.activity = (MatchActivity) activity;
  //  this.interactor = interactor;
  //  this.schedulerProvider = schedulerProvider;
  //}
  //
  //
  //@VisibleForTesting Observable<MatchIntent> intent() {
  //  //return Observable.merge(activity.loadFirstPageIntent(), activity.loadNextPageIntent(),
  //  //    activity.matchRowClickIntent()).subscribeOn(schedulerProvider.ui());
  //}
  //
  //@VisibleForTesting Observable<MatchesViewState> model(Observable<MatchesIntent> intents) {
  //  //return intents.flatMap(intent -> {
  //  //  if (intent instanceof MatchesIntent.LoadFirstPage) {
  //  //    return interactor.loadFirstPage().subscribeOn(schedulerProvider.io());
  //  //  }
  //  //  if (intent instanceof MatchesIntent.LoadNextPage) {
  //  //    return interactor.loadNextPage(((MatchesIntent.LoadNextPage) intent).currentPage())
  //  //        .subscribeOn(schedulerProvider.io());
  //  //  }
  //  //  if (intent instanceof MatchesIntent.MatchRowClick) {
  //  //    return Observable.just(
  //  //        MatchesViewState.matchRowClick(((MatchesIntent.MatchRowClick) intent).getMatch()));
  //  //  }
  //  //  throw new IllegalArgumentException("I don't know how to deal with this intent " + intent);
  //  //}).scan(MatchesViewState::reduce).subscribeOn(schedulerProvider.computation());
  //}
  //
  //@VisibleForTesting void view(Observable<MatchesViewState> states) {
  //  states.observeOn(schedulerProvider.ui()).subscribe(activity::render);
  //}
  //
  //void bind() {
  //  this.view(this.model(this.intent()));
  //}
}
