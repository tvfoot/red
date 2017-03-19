package io.oldering.tvfoot.red.data.repository;

import io.oldering.tvfoot.red.data.api.MatchService;
import io.oldering.tvfoot.red.data.entity.search.Filter;
import io.oldering.tvfoot.red.matches.MatchRowDisplayable;
import io.oldering.tvfoot.red.matches.MatchesViewState;
import io.reactivex.Observable;
import timber.log.Timber;

import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_ERROR;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_LOADED;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_LOADING;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_ERROR;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_LOADED;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_LOADING;

public class MatchesRepository {
  private final MatchService matchService;
  private int matchPerPage = 30;

  public MatchesRepository(MatchService matchService) {
    this.matchService = matchService;
  }

  public Observable<MatchesViewState> loadFirstPage() {
    return matchService.findFuture(Filter.builder().limit(matchPerPage).offset(0).build())
        .toObservable()
        .map(MatchRowDisplayable::fromMatches)
        .map(matches -> MatchesViewState.builder()
            .matches(matches)
            .status(FIRST_PAGE_LOADED)
            .build())
        .startWith(MatchesViewState.builder().status(FIRST_PAGE_LOADING).build())
        .onErrorReturn(error -> MatchesViewState.builder()
            .firstPageError(error)
            .status(FIRST_PAGE_ERROR)
            .build());
  }

  public Observable<MatchesViewState> loadNextPage(int currentPage) {
    Timber.d("load NExt Page %s", currentPage);
    return matchService.findFuture(
        Filter.builder().limit(matchPerPage).offset(matchPerPage * currentPage).build())
        .toObservable()
        .map(MatchRowDisplayable::fromMatches)
        .map(
            matches -> MatchesViewState.builder().matches(matches).status(NEXT_PAGE_LOADED).build())
        .startWith(MatchesViewState.builder().status(NEXT_PAGE_LOADING).build())
        .onErrorReturn(error -> MatchesViewState.builder()
            .firstPageError(error)
            .status(NEXT_PAGE_ERROR)
            .build());
  }
}
