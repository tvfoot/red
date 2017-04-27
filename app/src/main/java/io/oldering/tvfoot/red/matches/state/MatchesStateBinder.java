package io.oldering.tvfoot.red.matches.state;

import io.oldering.tvfoot.red.data.entity.Match;
import io.oldering.tvfoot.red.di.scope.ScreenScope;
import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;
import io.oldering.tvfoot.red.util.Preconditions;
import io.oldering.tvfoot.red.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import io.reactivex.subjects.PublishSubject;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import timber.log.Timber;

@ScreenScope public class MatchesStateBinder {
  private PublishSubject<MatchesIntent> intentsSubject = PublishSubject.create();
  private PublishSubject<MatchesViewState> statesSubject = PublishSubject.create();
  private MatchesService service;
  private BaseSchedulerProvider schedulerProvider;

  @Inject MatchesStateBinder(MatchesService service, BaseSchedulerProvider schedulerProvider) {
    this.service = service;
    this.schedulerProvider = schedulerProvider;

    compose().subscribe(state -> statesSubject.onNext(state));
  }

  public void forwardIntents(Observable<MatchesIntent> intents) {
    intents.subscribe(intentsSubject::onNext);
  }

  public Observable<MatchesViewState> getStatesAsObservable() {
    return statesSubject;
  }

  private Observable<MatchesViewState> compose() {
    return intentsSubject.doOnNext(intent -> Timber.d("MatchesIntent: %s", intent))
        .scan(initialIntentFilter)
        .map(this::actionFromIntent)
        .doOnNext(action -> Timber.d("MatchesAction: %s", action))
        .compose(actionToResultTransformer)
        .doOnNext(matchesResult -> Timber.d("MatchesResult: %s", matchesResult))
        .scan(MatchesViewState.idle(), reducer)
        .doOnNext(state -> Timber.d("MatchesState: %s", state));
  }

  private BiFunction<MatchesIntent, MatchesIntent, MatchesIntent> initialIntentFilter =
      (previousIntent, newIntent) -> {
        // if isReConnection (e.g. after config change)
        // i.e. we are inside the scan, meaning there has already
        // been intent in the past, meaning the InitialIntent cannot
        // be the first => it is a reconnection.
        if (newIntent instanceof MatchesIntent.InitialIntent) {
          return MatchesIntent.GetLastState.create();
        } else {
          return newIntent;
        }
      };

  private MatchesAction actionFromIntent(MatchesIntent intent) {
    if (intent instanceof MatchesIntent.InitialIntent) {
      // TODO(benoit) remove LoadFirst* and use LoadNext(0)
      return MatchesAction.LoadFirstPageAction.create();
    }
    if (intent instanceof MatchesIntent.GetLastState) {
      return MatchesAction.GetLastStateAction.create();
    }
    if (intent instanceof MatchesIntent.LoadNextPageIntent) {
      return MatchesAction.LoadNextPageAction.create(
          ((MatchesIntent.LoadNextPageIntent) intent).pageIndex());
    }
    if (intent instanceof MatchesIntent.MatchRowClickIntent) {
      return MatchesAction.MatchRowClickAction.create(
          ((MatchesIntent.MatchRowClickIntent) intent).match());
    }
    throw new IllegalArgumentException("do not know how to treat this intents " + intent);
  }

  private ObservableTransformer<MatchesAction.LoadFirstPageAction, MatchesResult.LoadFirstPageResult>
      loadFirstPageTransformer = actions -> actions.flatMap(action -> service.loadFirstPage()
      .toObservable()
      .map(MatchesResult.LoadFirstPageResult::success)
      .onErrorReturn(MatchesResult.LoadFirstPageResult::failure)
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
      .startWith(MatchesResult.LoadFirstPageResult.inFlight()));

  private ObservableTransformer<MatchesAction.LoadNextPageAction, MatchesResult.LoadNextPageResult>
      loadNextPageTransformer = actions -> actions.flatMap(
      action -> service.loadNextPage(action.pageIndex())
          .toObservable()
          .map(MatchesResult.LoadNextPageResult::success)
          .onErrorReturn(MatchesResult.LoadNextPageResult::failure)
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
          .startWith(MatchesResult.LoadNextPageResult.inFlight(action.pageIndex())));

  private ObservableTransformer<MatchesAction.MatchRowClickAction, MatchesResult.MatchRowClickResult>
      matchRowClickTransformer = actions -> actions.flatMap(
      action -> Observable.just(MatchesResult.MatchRowClickResult.create(action.match())));

  private ObservableTransformer<MatchesAction.GetLastStateAction, MatchesResult.GetLastStateResult>
      getLastStateTransformer =
      actions -> actions.map(ignored -> MatchesResult.GetLastStateResult.create());

  private ObservableTransformer<MatchesAction, MatchesResult> actionToResultTransformer =
      actions -> actions.publish(shared -> Observable.merge(
          shared.ofType(MatchesAction.LoadFirstPageAction.class).compose(loadFirstPageTransformer),
          shared.ofType(MatchesAction.LoadNextPageAction.class).compose(loadNextPageTransformer),
          shared.ofType(MatchesAction.MatchRowClickAction.class).compose(matchRowClickTransformer),
          shared.ofType(MatchesAction.GetLastStateAction.class).compose(getLastStateTransformer))
          .mergeWith(
              // Error for not implemented actions
              shared.filter(v -> !(v instanceof MatchesAction.LoadFirstPageAction)
                  && !(v instanceof MatchesAction.LoadNextPageAction)
                  && !(v instanceof MatchesAction.MatchRowClickAction)
                  && !(v instanceof MatchesAction.GetLastStateAction))
                  .flatMap(w -> Observable.error(
                      new IllegalArgumentException("Unknown Action type: " + w)))));

  private static BiFunction<MatchesViewState, MatchesResult, MatchesViewState> reducer =
      (previousState, matchesResult) -> {
        MatchesViewState.Builder stateBuilder = previousState.buildWith();

        // check matchesResult and update state accordingly
        if (matchesResult instanceof MatchesResult.LoadFirstPageResult) {
          switch (((MatchesResult.LoadFirstPageResult) matchesResult).status()) {
            case FIRST_PAGE_IN_FLIGHT:
              stateBuilder.firstPageLoading(true)
                  .error(null)
                  .status(MatchesViewState.Status.FIRST_PAGE_IN_FLIGHT);
              break;
            case FIRST_PAGE_FAILURE:
              stateBuilder.firstPageLoading(false)
                  .error(((MatchesResult.LoadFirstPageResult) matchesResult).throwable())
                  .status(MatchesViewState.Status.FIRST_PAGE_FAILURE);
              break;
            case FIRST_PAGE_SUCCESS:
              List<Match> matches = Preconditions.checkNotNull(
                  ((MatchesResult.LoadFirstPageResult) matchesResult).matches(),
                  "Matches are null");

              stateBuilder.firstPageLoading(false)
                  .error(null)
                  .matches(MatchRowDisplayable.fromMatches(matches))
                  .status(MatchesViewState.Status.FIRST_PAGE_SUCCESS);
              break;
            default:
              throw new IllegalArgumentException("Wrong status for LoadFirstPageResult: "
                  + ((MatchesResult.LoadFirstPageResult) matchesResult).status());
          }
        } else if (matchesResult instanceof MatchesResult.LoadNextPageResult) {
          switch (((MatchesResult.LoadNextPageResult) matchesResult).status()) {
            case NEXT_PAGE_IN_FLIGHT:
              stateBuilder.nextPageLoading(true)
                  .currentPage(((MatchesResult.LoadNextPageResult) matchesResult).pageIndex())
                  .error(null)
                  .status(MatchesViewState.Status.NEXT_PAGE_IN_FLIGHT);
              break;
            case NEXT_PAGE_FAILURE:
              stateBuilder.nextPageLoading(false)
                  .error(((MatchesResult.LoadNextPageResult) matchesResult).error())
                  .status(MatchesViewState.Status.NEXT_PAGE_FAILURE);
              break;
            case NEXT_PAGE_SUCCESS:
              List<Match> newMatches = Preconditions.checkNotNull(
                  ((MatchesResult.LoadNextPageResult) matchesResult).matches(), "Matches are null");

              List<MatchRowDisplayable> matches = new ArrayList<>();
              matches.addAll(previousState.matches());
              matches.addAll(MatchRowDisplayable.fromMatches(newMatches));

              stateBuilder.nextPageLoading(false)
                  .error(null)
                  .matches(matches)
                  .status(MatchesViewState.Status.NEXT_PAGE_SUCCESS);
              break;
            default:
              throw new IllegalArgumentException("Wrong status for LoadNextPageResult: "
                  + ((MatchesResult.LoadNextPageResult) matchesResult).status());
          }
        } else if (matchesResult instanceof MatchesResult.MatchRowClickResult) {
          stateBuilder.clickedMatch(
              ((MatchesResult.MatchRowClickResult) matchesResult).clickedMatch())
              .status(MatchesViewState.Status.MATCH_ROW_CLICK);
        } else if (matchesResult instanceof MatchesResult.GetLastStateResult) {
          return stateBuilder.status(MatchesViewState.Status.LAST_STATE).build();
        } else {
          throw new IllegalArgumentException("Don't know this matchesResult " + matchesResult);
        }

        return stateBuilder.build();
      };
}
