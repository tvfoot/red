package com.benoitquenaudon.tvfoot.red.app.domain.matches.state;

import android.os.Bundle;
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider;
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchRowDisplayable;
import com.benoitquenaudon.tvfoot.red.app.injection.scope.ScreenScope;
import com.google.firebase.analytics.FirebaseAnalytics;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

import static com.benoitquenaudon.tvfoot.red.app.common.PreConditions.checkNotNull;

@ScreenScope public class MatchesStateBinder {
  private PublishSubject<MatchesIntent> intentsSubject;
  private PublishSubject<MatchesViewState> statesSubject;
  private MatchesService service;
  private BaseSchedulerProvider schedulerProvider;
  private FirebaseAnalytics firebaseAnalytics;

  @SuppressWarnings("CheckReturnValue") //
  @Inject MatchesStateBinder(PublishSubject<MatchesIntent> intentsSubject,
      PublishSubject<MatchesViewState> statesSubject, MatchesService service,
      BaseSchedulerProvider schedulerProvider, FirebaseAnalytics firebaseAnalytics) {
    this.intentsSubject = intentsSubject;
    this.statesSubject = statesSubject;
    this.service = service;
    this.schedulerProvider = schedulerProvider;
    this.firebaseAnalytics = firebaseAnalytics;

    compose().subscribe(state -> this.statesSubject.onNext(state));
  }

  @SuppressWarnings("CheckReturnValue")
  public void forwardIntents(Observable<MatchesIntent> intents) {
    intents.subscribe(intentsSubject::onNext);
  }

  public Observable<MatchesViewState> getStatesAsObservable() {
    return statesSubject;
  }

  private Observable<MatchesViewState> compose() {
    return intentsSubject.doOnNext(this::logIntent)
        .scan(initialIntentFilter)
        .map(this::actionFromIntent)
        .doOnNext(this::logAction)
        .compose(actionToResultTransformer)
        .doOnNext(this::logResult)
        .scan(MatchesViewState.idle(), reducer)
        .doOnNext(this::logState);
  }

  private BiFunction<MatchesIntent, MatchesIntent, MatchesIntent> initialIntentFilter =
      (previousIntent, newIntent) -> {
        // if isReConnection (e.g. after config change)
        // i.e. we are inside the scan, meaning there has already
        // been intent in the past, meaning the InitialIntent cannot
        // be the first => it is a reconnection.
        if (newIntent instanceof MatchesIntent.InitialIntent) {
          return MatchesIntent.GetLastState.INSTANCE;
        } else {
          return newIntent;
        }
      };

  private MatchesAction actionFromIntent(MatchesIntent intent) {
    if (intent instanceof MatchesIntent.InitialIntent) {
      return MatchesAction.RefreshAction.INSTANCE;
    }
    if (intent instanceof MatchesIntent.RefreshIntent) {
      return MatchesAction.RefreshAction.INSTANCE;
    }
    if (intent instanceof MatchesIntent.GetLastState) {
      return MatchesAction.GetLastStateAction.INSTANCE;
    }
    if (intent instanceof MatchesIntent.LoadNextPageIntent) {
      return new MatchesAction.LoadNextPageAction(
          ((MatchesIntent.LoadNextPageIntent) intent).getPageIndex());
    }
    throw new IllegalArgumentException("do not know how to treat this intents " + intent);
  }

  private ObservableTransformer<MatchesAction.RefreshAction, MatchesResult.RefreshResult>
      refreshTransformer = actions -> actions.flatMap(action -> service.loadPage(0)
      .toObservable()
      .map(MatchesResult.RefreshResult.Factory::success)
      .onErrorReturn(MatchesResult.RefreshResult.Factory::failure)
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
      .startWith(MatchesResult.RefreshResult.Factory.inFlight()));

  private ObservableTransformer<MatchesAction.LoadNextPageAction, MatchesResult.LoadNextPageResult>
      loadNextPageTransformer = actions -> actions.flatMap(
      action -> service.loadPage(action.getPageIndex())
          .toObservable()
          .map(matches -> MatchesResult.LoadNextPageResult.Factory.success(action.getPageIndex(),
              matches))
          .onErrorReturn(MatchesResult.LoadNextPageResult.Factory::failure)
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
          .startWith(MatchesResult.LoadNextPageResult.Factory.inFlight()));

  private ObservableTransformer<MatchesAction.GetLastStateAction, MatchesResult.GetLastStateResult>
      getLastStateTransformer =
      actions -> actions.map(ignored -> MatchesResult.GetLastStateResult.INSTANCE);

  private ObservableTransformer<MatchesAction, MatchesResult> actionToResultTransformer =
      actions -> actions.publish(shared -> Observable.merge(
          shared.ofType(MatchesAction.RefreshAction.class).compose(refreshTransformer),
          shared.ofType(MatchesAction.LoadNextPageAction.class).compose(loadNextPageTransformer),
          shared.ofType(MatchesAction.GetLastStateAction.class).compose(getLastStateTransformer))
          .mergeWith(
              // Error for not implemented actions
              shared.filter(v -> !(v instanceof MatchesAction.RefreshAction)
                  && !(v instanceof MatchesAction.LoadNextPageAction)
                  && !(v instanceof MatchesAction.GetLastStateAction))
                  .flatMap(w -> Observable.error(
                      new IllegalArgumentException("Unknown Action type: " + w)))));

  private static BiFunction<MatchesViewState, MatchesResult, MatchesViewState> reducer =
      (previousState, matchesResult) -> {
        MatchesViewState.Builder stateBuilder = previousState.buildWith();

        // check matchesResult and update state accordingly
        if (matchesResult instanceof MatchesResult.RefreshResult) {
          switch (((MatchesResult.RefreshResult) matchesResult).getStatus()) {
            case IN_FLIGHT:
              return stateBuilder.refreshLoading(true).error(null).build();
            case FAILURE:
              return stateBuilder.refreshLoading(false)
                  .error(((MatchesResult.RefreshResult) matchesResult).getThrowable())
                  .build();
            case SUCCESS:
              List<Match> matches =
                  checkNotNull(((MatchesResult.RefreshResult) matchesResult).getMatches(),
                      "Matches are null");

              return stateBuilder.refreshLoading(false)
                  .error(null)
                  .hasMore(!matches.isEmpty())
                  .currentPage(0)
                  .matches(MatchRowDisplayable.Factory.fromMatches(matches))
                  .build();
            default:
              throw new IllegalArgumentException(
                  "Wrong status for RefreshResult: " + ((MatchesResult.RefreshResult) matchesResult)
                      .getStatus());
          }
        } else if (matchesResult instanceof MatchesResult.LoadNextPageResult) {
          switch (((MatchesResult.LoadNextPageResult) matchesResult).getStatus()) {
            case IN_FLIGHT:
              return stateBuilder.nextPageLoading(true).error(null).build();
            case FAILURE:
              return stateBuilder.nextPageLoading(false)
                  .error(((MatchesResult.LoadNextPageResult) matchesResult).getError())
                  .build();
            case SUCCESS:
              List<Match> newMatches =
                  checkNotNull(((MatchesResult.LoadNextPageResult) matchesResult).getMatches(),
                      "Matches are null");

              List<MatchRowDisplayable> matches = new ArrayList<>();
              matches.addAll(previousState.matches());
              matches.addAll(MatchRowDisplayable.Factory.fromMatches(newMatches));

              return stateBuilder.nextPageLoading(false)
                  .error(null)
                  .matches(matches)
                  .currentPage(((MatchesResult.LoadNextPageResult) matchesResult).getPageIndex())
                  .hasMore(!newMatches.isEmpty())
                  .build();
            default:
              throw new IllegalArgumentException("Wrong status for LoadNextPageResult: "
                  + ((MatchesResult.LoadNextPageResult) matchesResult).getStatus());
          }
        } else if (matchesResult instanceof MatchesResult.GetLastStateResult) {
          return stateBuilder.build();
        } else {
          throw new IllegalArgumentException("Don't know this matchesResult " + matchesResult);
        }
      };

  private void logIntent(MatchesIntent intent) {
    Timber.d("Intent: %s", intent);
    Bundle params = new Bundle();
    params.putString("intent", intent.toString());
    firebaseAnalytics.logEvent("intent", params);
  }

  private void logAction(MatchesAction action) {
    Timber.d("Action: %s", action);
    Bundle params = new Bundle();
    params.putString("action", action.toString());
    firebaseAnalytics.logEvent("action", params);
  }

  private void logResult(MatchesResult result) {
    Timber.d("Result: %s", result);
    Bundle params = new Bundle();
    params.putString("result", result.toString());
    firebaseAnalytics.logEvent("result", params);
  }

  private void logState(MatchesViewState state) {
    Timber.d("State: %s", state);
    Bundle params = new Bundle();
    params.putString("state", state.toString());
    firebaseAnalytics.logEvent("state", params);
  }
}
