package com.benoitquenaudon.tvfoot.red.app.domain.match.state;

import android.os.Bundle;
import com.benoitquenaudon.tvfoot.red.app.common.PreferenceService;
import com.benoitquenaudon.tvfoot.red.app.common.firebase.BaseRedFirebaseAnalytics;
import com.benoitquenaudon.tvfoot.red.app.common.notification.NotificationService;
import com.benoitquenaudon.tvfoot.red.app.common.schedulers.BaseSchedulerProvider;
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match;
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchDisplayable;
import com.benoitquenaudon.tvfoot.red.app.injection.scope.ScreenScope;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;
import javax.inject.Inject;
import timber.log.Timber;

import static com.benoitquenaudon.tvfoot.red.app.common.PreConditions.checkNotNull;

@ScreenScope public class MatchStateBinder {
  private PublishSubject<MatchIntent> intentsSubject;
  private PublishSubject<MatchViewState> statesSubject;
  private MatchService matchService;
  private PreferenceService preferenceService;
  private BaseSchedulerProvider schedulerProvider;
  private BaseRedFirebaseAnalytics firebaseAnalytics;
  private NotificationService notificationService;

  @Inject MatchStateBinder(PublishSubject<MatchIntent> intentsSubject,
      PublishSubject<MatchViewState> statesSubject, MatchService matchService,
      PreferenceService preferenceService, NotificationService notificationService,
      BaseSchedulerProvider schedulerProvider, BaseRedFirebaseAnalytics firebaseAnalytics) {
    this.intentsSubject = intentsSubject;
    this.statesSubject = statesSubject;
    this.matchService = matchService;
    this.preferenceService = preferenceService;
    this.notificationService = notificationService;
    this.schedulerProvider = schedulerProvider;
    this.firebaseAnalytics = firebaseAnalytics;

    compose().subscribe(state -> this.statesSubject.onNext(state));
  }

  @SuppressWarnings("CheckReturnValue")
  public void forwardIntents(Observable<MatchIntent> intents) {
    intents.subscribe(intentsSubject::onNext);
  }

  public Observable<MatchViewState> getStatesAsObservable() {
    return statesSubject;
  }

  private Observable<MatchViewState> compose() {
    return intentsSubject.doOnNext(this::logIntent)
        .scan(initialIntentFilter)
        .map(this::actionFromIntent)
        .doOnNext(this::logAction)
        .compose(actionToResultTransformer)
        .doOnNext(this::logResult)
        .scan(MatchViewState.idle(), reducer)
        .doOnNext(this::logState);
  }

  private BiFunction<MatchIntent, MatchIntent, MatchIntent> initialIntentFilter =
      (previousIntent, newIntent) -> {
        // if isReConnection (e.g. after config change)
        // i.e. we are inside the scan, meaning there has already
        // been intent in the past, meaning the InitialIntent cannot
        // be the first => it is a reconnection.
        if (newIntent instanceof MatchIntent.InitialIntent) {
          return MatchIntent.GetLastState.create();
        } else {
          return newIntent;
        }
      };

  private MatchAction actionFromIntent(MatchIntent intent) {
    if (intent instanceof MatchIntent.InitialIntent) {
      return MatchAction.LoadMatchAction.create(((MatchIntent.InitialIntent) intent).matchId());
    }
    if (intent instanceof MatchIntent.GetLastState) {
      return MatchAction.GetLastStateAction.create();
    }
    if (intent instanceof MatchIntent.NotifyMatchStartIntent) {
      return MatchAction.NotifyMatchStartAction.create(
          ((MatchIntent.NotifyMatchStartIntent) intent).matchId(),
          ((MatchIntent.NotifyMatchStartIntent) intent).startAt(),
          ((MatchIntent.NotifyMatchStartIntent) intent).notifyMatchStart());
    }
    throw new IllegalArgumentException("do not know how to treat this intents " + intent);
  }

  private ObservableTransformer<MatchAction.LoadMatchAction, MatchResult.LoadMatchResult>
      loadMatchTransformer = actions -> actions.flatMap(
      action -> Single.zip(matchService.loadMatch(action.matchId()),
          preferenceService.loadNotifyMatchStart(action.matchId()),
          MatchResult.LoadMatchResult::success)
          .toObservable()
          .onErrorReturn(MatchResult.LoadMatchResult::failure)
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
          .startWith(MatchResult.LoadMatchResult.inFlight()));

  private ObservableTransformer<MatchAction.NotifyMatchStartAction, MatchResult.NotifyMatchStartResult>
      notifyMatchStartTransformer = actions -> actions.flatMap(
      action -> preferenceService.saveNotifyMatchStart(action.matchId(), action.notifyMatchStart())
          .flatMap(ignored -> notificationService.scheduleNotification(action.matchId(),
              action.startAt(), action.notifyMatchStart()))
          .toObservable()
          .map(ignored -> MatchResult.NotifyMatchStartResult.create(action.notifyMatchStart())));

  private ObservableTransformer<MatchAction.GetLastStateAction, MatchResult.GetLastStateResult>
      getLastStateTransformer =
      actions -> actions.map(ignored -> MatchResult.GetLastStateResult.create());

  private ObservableTransformer<MatchAction, MatchResult> actionToResultTransformer =
      actions -> actions.publish(shared -> Observable.merge(
          shared.ofType(MatchAction.LoadMatchAction.class).compose(loadMatchTransformer),
          shared.ofType(MatchAction.GetLastStateAction.class).compose(getLastStateTransformer),
          shared.ofType(MatchAction.NotifyMatchStartAction.class)
              .compose(notifyMatchStartTransformer)) //
          .mergeWith(
              // Error for not implemented actions
              shared.filter(v -> !(v instanceof MatchAction.LoadMatchAction)
                  && !(v instanceof MatchAction.GetLastStateAction)
                  && !(v instanceof MatchAction.NotifyMatchStartAction))
                  .flatMap(w -> Observable.error(
                      new IllegalArgumentException("Unknown Action type: " + w)))));

  private static BiFunction<MatchViewState, MatchResult, MatchViewState> reducer =
      (previousState, matchResult) -> {
        MatchViewState.Builder stateBuilder = previousState.buildWith();

        if (matchResult instanceof MatchResult.LoadMatchResult) {
          switch (((MatchResult.LoadMatchResult) matchResult).status()) {
            case LOAD_MATCH_IN_FLIGHT:
              stateBuilder.loading(true).error(null);
              break;
            case LOAD_MATCH_FAILURE:
              stateBuilder.loading(false)
                  .error(((MatchResult.LoadMatchResult) matchResult).error());
              break;
            case LOAD_MATCH_SUCCESS:
              Match match = checkNotNull(((MatchResult.LoadMatchResult) matchResult).match(),
                  "Match are null");

              stateBuilder.loading(false)
                  .error(null)
                  .shouldNotifyMatchStart(
                      ((MatchResult.LoadMatchResult) matchResult).shouldNotifyMatchStart())
                  .match(MatchDisplayable.fromMatch(match));
              break;
            default:
              throw new IllegalArgumentException(
                  "Wrong status for LoadMatchResult: " + ((MatchResult.LoadMatchResult) matchResult)
                      .status());
          }
        } else if (matchResult instanceof MatchResult.GetLastStateResult) {
          return stateBuilder.build();
        } else if (matchResult instanceof MatchResult.NotifyMatchStartResult) {
          return stateBuilder.shouldNotifyMatchStart(
              ((MatchResult.NotifyMatchStartResult) matchResult).shouldNotifyMatchStart()).build();
        } else {
          throw new IllegalArgumentException("Don't know this matchResult " + matchResult);
        }

        return stateBuilder.build();
      };

  private void logIntent(MatchIntent intent) {
    Timber.d("Intent: %s", intent);
    Bundle params = new Bundle();
    params.putString("intent", intent.toString());
    firebaseAnalytics.logEvent("intent", params);
  }

  private void logAction(MatchAction action) {
    Timber.d("Action: %s", action);
    Bundle params = new Bundle();
    params.putString("action", action.toString());
    firebaseAnalytics.logEvent("action", params);
  }

  private void logResult(MatchResult result) {
    Timber.d("Result: %s", result);
    Bundle params = new Bundle();
    params.putString("result", result.toString());
    firebaseAnalytics.logEvent("result", params);
  }

  private void logState(MatchViewState state) {
    Timber.d("State: %s", state);
    Bundle params = new Bundle();
    params.putString("state", state.toString());
    firebaseAnalytics.logEvent("state", params);
  }
}
