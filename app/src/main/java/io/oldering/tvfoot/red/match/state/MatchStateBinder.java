package io.oldering.tvfoot.red.match.state;

import io.oldering.tvfoot.red.data.entity.Match;
import io.oldering.tvfoot.red.match.MatchDisplayable;
import io.oldering.tvfoot.red.util.Preconditions;
import io.oldering.tvfoot.red.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;
import javax.inject.Inject;
import timber.log.Timber;

// TODO(benoit) fix the scope quickly because it is recreating on config change.
public class MatchStateBinder {
  private PublishSubject<MatchIntent> intentsSubject = PublishSubject.create();
  private PublishSubject<MatchViewState> statesSubject = PublishSubject.create();
  private MatchService service;
  private BaseSchedulerProvider schedulerProvider;

  @Inject public MatchStateBinder(MatchService service, BaseSchedulerProvider schedulerProvider) {
    this.service = service;
    this.schedulerProvider = schedulerProvider;

    compose().subscribe(state -> statesSubject.onNext(state));
  }

  public void forwardIntents(Observable<MatchIntent> intents) {
    intents.subscribe(intentsSubject::onNext);
  }

  public Observable<MatchViewState> getStatesAsObservable() {
    return statesSubject;
  }

  private Observable<MatchViewState> compose() {
    return intentsSubject.doOnNext(intent -> Timber.d("MatchIntent: %s", intent))
        .scan(initialIntentFilter)
        .map(this::actionFromIntent)
        .doOnNext(action -> Timber.d("MatchAction: %s", action))
        .compose(actionToResultTransformer)
        .doOnNext(matchResult -> Timber.d("MatchResult: %s", matchResult))
        .scan(MatchViewState.idle(), reducer)
        .doOnNext(state -> Timber.d("MatchState: %s", state));
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
      // TODO(benoit) what should I do here?
      return MatchAction.LoadMatchAction.create(((MatchIntent.InitialIntent) intent).matchId());
    }
    if (intent instanceof MatchIntent.GetLastState) {
      return MatchAction.GetLastStateAction.create();
    }
    throw new IllegalArgumentException("do not know how to treat this intents " + intent);
  }

  private ObservableTransformer<MatchAction.LoadMatchAction, MatchResult.LoadMatchResult>
      loadMatchTransformer = actions -> actions.flatMap(
      action -> service.loadMatch(action.matchId())
          .toObservable()
          .map(MatchResult.LoadMatchResult::success)
          .onErrorReturn(MatchResult.LoadMatchResult::failure)
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
          .startWith(MatchResult.LoadMatchResult.inFlight()));

  private ObservableTransformer<MatchAction.GetLastStateAction, MatchResult.GetLastStateResult>
      getLastStateTransformer =
      actions -> actions.map(ignored -> MatchResult.GetLastStateResult.create());

  private ObservableTransformer<MatchAction, MatchResult> actionToResultTransformer =
      actions -> actions.publish(shared -> Observable.merge(
          shared.ofType(MatchAction.LoadMatchAction.class).compose(loadMatchTransformer),
          shared.ofType(MatchAction.GetLastStateAction.class).compose(getLastStateTransformer))
          .mergeWith(
              // Error for not implemented actions
              shared.filter(v -> !(v instanceof MatchAction.LoadMatchAction)
                  && !(v instanceof MatchAction.GetLastStateAction))
                  .flatMap(w -> Observable.error(
                      new IllegalArgumentException("Unknown Action type: " + w)))));

  private static BiFunction<MatchViewState, MatchResult, MatchViewState> reducer =
      (previousState, matchResult) -> {
        MatchViewState.Builder stateBuilder = previousState.buildWith();

        if (matchResult instanceof MatchResult.LoadMatchResult) {
          switch (((MatchResult.LoadMatchResult) matchResult).status()) {
            case LOAD_MATCH_IN_FLIGHT:
              stateBuilder.loading(true)
                  .error(null)
                  .status(MatchViewState.Status.LOAD_MATCH_IN_FLIGHT);
              break;
            case LOAD_MATCH_FAILURE:
              stateBuilder.loading(false)
                  .error(((MatchResult.LoadMatchResult) matchResult).error())
                  .status(MatchViewState.Status.LOAD_MATCH_FAILURE);
              break;
            case LOAD_MATCH_SUCCESS:
              Match match =
                  Preconditions.checkNotNull(((MatchResult.LoadMatchResult) matchResult).match(),
                      "Match are null");

              stateBuilder.loading(false)
                  .error(null)
                  .match(MatchDisplayable.fromMatch(match))
                  .status(MatchViewState.Status.LOAD_MATCH_SUCCESS);
              break;
            default:
              throw new IllegalArgumentException("Wrong status for LoadFirstPageResult: "
                  + ((MatchResult.LoadMatchResult) matchResult).status());
          }
        } else if (matchResult instanceof MatchResult.GetLastStateResult) {
          return stateBuilder.build();
        } else {
          throw new IllegalArgumentException("Don't know this matchResult " + matchResult);
        }

        return stateBuilder.build();
      };
}
