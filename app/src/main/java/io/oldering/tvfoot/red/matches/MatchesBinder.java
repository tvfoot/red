package io.oldering.tvfoot.red.matches;

import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;
import io.oldering.tvfoot.red.util.schedulers.BaseSchedulerProvider;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.functions.BiFunction;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import timber.log.Timber;

@Singleton class MatchesBinder {
  private MatchesService matchesService;
  private BaseSchedulerProvider schedulerProvider;

  @Inject MatchesBinder(MatchesService matchesService, BaseSchedulerProvider schedulerProvider) {
    this.matchesService = matchesService;
    this.schedulerProvider = schedulerProvider;
  }

  private ObservableTransformer<MatchesAction.LoadFirstPageAction, MatchesResult.LoadFirstPageResult>
      loadFirstPageTransformer = actions -> actions.flatMap(action -> matchesService.loadFirstPage()
      .toObservable()
      .map(MatchesResult.LoadFirstPageResult::success)
      .onErrorReturn(MatchesResult.LoadFirstPageResult::failure)
      .subscribeOn(schedulerProvider.io())
      .observeOn(schedulerProvider.ui())
      .startWith(MatchesResult.LoadFirstPageResult.inFlight()));

  private ObservableTransformer<MatchesAction.LoadNextPageAction, MatchesResult.LoadNextPageResult>
      loadNextPageTransformer = actions -> actions.flatMap(
      action -> matchesService.loadNextPage(action.currentPage())
          .toObservable()
          .map(MatchesResult.LoadNextPageResult::success)
          .onErrorReturn(MatchesResult.LoadNextPageResult::failure)
          .subscribeOn(schedulerProvider.io())
          .observeOn(schedulerProvider.ui())
          .startWith(MatchesResult.LoadNextPageResult.inFlight()));

  private ObservableTransformer<MatchesAction.MatchRowClickAction, MatchesResult.MatchRowClickResult>
      matchRowClickTransformer = actions -> actions.flatMap(
      action -> Observable.just(new MatchesResult.MatchRowClickResult(action.getMatch())));

  private ObservableTransformer<MatchesAction, MatchesResult> actionToResultTransformer =
      actions -> actions.publish(shared -> Observable.merge(
          shared.ofType(MatchesAction.LoadFirstPageAction.class).compose(loadFirstPageTransformer),
          shared.ofType(MatchesAction.LoadNextPageAction.class).compose(loadNextPageTransformer),
          shared.ofType(MatchesAction.MatchRowClickAction.class).compose(matchRowClickTransformer),
          // Error for not implemented actions
          shared.filter(v -> !(v instanceof MatchesAction.LoadFirstPageAction)
              && !(v instanceof MatchesAction.LoadNextPageAction)
              && !(v instanceof MatchesAction.MatchRowClickAction))
              .flatMap(w -> Observable.error(new IllegalArgumentException("Unknown type: " + w)))));

  private MatchesAction actionFromIntent(MatchesIntent intent) {
    if (intent instanceof MatchesIntent.LoadFirstPageIntent) {
      return MatchesAction.LoadFirstPageAction.create();
    }
    if (intent instanceof MatchesIntent.LoadNextPageIntent) {
      return MatchesAction.LoadNextPageAction.create(
          ((MatchesIntent.LoadNextPageIntent) intent).currentPage());
    }
    if (intent instanceof MatchesIntent.MatchRowClickIntent) {
      return MatchesAction.MatchRowClickAction.create(
          ((MatchesIntent.MatchRowClickIntent) intent).getMatch());
    }
    throw new IllegalArgumentException("do not know how to treat this intents " + intent);
  }

  private static BiFunction<MatchesViewState, MatchesResult, MatchesViewState> reducer =
      (previousState, matchesResult) -> {
        MatchesViewState.Builder stateBuilder = previousState.buildWith();

        // check matchesResult and update state accordingly
        if (matchesResult instanceof MatchesResult.LoadFirstPageResult) {
          switch (((MatchesResult.LoadFirstPageResult) matchesResult).getStatus()) {
            case FIRST_PAGE_IN_FLIGHT:
              stateBuilder.firstPageLoading(true)
                  .firstPageError(null)
                  .status(MatchesViewState.Status.FIRST_PAGE_IN_FLIGHT);
              break;
            case FIRST_PAGE_FAILURE:
              stateBuilder.firstPageLoading(false)
                  .firstPageError(((MatchesResult.LoadFirstPageResult) matchesResult).getThrowable())
                  .status(MatchesViewState.Status.FIRST_PAGE_FAILURE);
              break;
            case FIRST_PAGE_SUCCESS:
              stateBuilder.firstPageLoading(false)
                  .firstPageError(null)
                  .matches(MatchRowDisplayable.fromMatches(
                      ((MatchesResult.LoadFirstPageResult) matchesResult).getMatches()))
                  .status(MatchesViewState.Status.FIRST_PAGE_SUCCESS);
              break;
            default:
              throw new IllegalArgumentException("Wrong status for LoadFirstPageResult: "
                  + ((MatchesResult.LoadFirstPageResult) matchesResult).getStatus());
          }
        } else if (matchesResult instanceof MatchesResult.LoadNextPageResult) {
          switch (((MatchesResult.LoadNextPageResult) matchesResult).getStatus()) {
            case NEXT_PAGE_IN_FLIGHT:
              stateBuilder.nextPageLoading(true)
                  .nextPageError(null)
                  .status(MatchesViewState.Status.NEXT_PAGE_IN_FLIGHT);
              break;
            case NEXT_PAGE_FAILURE:
              stateBuilder.nextPageLoading(false)
                  .nextPageError(((MatchesResult.LoadNextPageResult) matchesResult).getThrowable())
                  .status(MatchesViewState.Status.NEXT_PAGE_FAILURE);
              break;
            case NEXT_PAGE_SUCCESS:
              List<MatchRowDisplayable> matches = new ArrayList<>();
              matches.addAll(previousState.matches());
              matches.addAll(MatchRowDisplayable.fromMatches(
                  ((MatchesResult.LoadNextPageResult) matchesResult).getMatches()));

              stateBuilder.nextPageLoading(false)
                  .nextPageError(null)
                  .matches(matches)
                  .status(MatchesViewState.Status.NEXT_PAGE_SUCCESS);
              break;
            default:
              throw new IllegalArgumentException("Wrong status for LoadNextPageResult: "
                  + ((MatchesResult.LoadNextPageResult) matchesResult).getStatus());
          }
        } else if (matchesResult instanceof MatchesResult.MatchRowClickResult) {
          stateBuilder.match(((MatchesResult.MatchRowClickResult) matchesResult).getMatch())
              .status(MatchesViewState.Status.MATCH_ROW_CLICK);
        } else {
          throw new IllegalArgumentException("Don't know this matchesResult " + matchesResult);
        }

        return stateBuilder.build();
      };

  Observable<MatchesViewState> compose(Observable<MatchesIntent> intents) {
    return intents.doOnNext(intent -> Timber.d("Intent: %s", intent))
        .map(this::actionFromIntent)
        .doOnNext(action -> Timber.d("MatchesAction: %s", action))
        .compose(actionToResultTransformer)
        .doOnNext(matchesResult -> Timber.d("MatchesResult: %s", matchesResult))
        .scan(MatchesViewState.idle(), reducer)
        .doOnNext(state -> Timber.d("State: %s", state));
  }
}
