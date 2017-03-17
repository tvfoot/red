package io.oldering.tvfoot.red.matches;

import io.oldering.tvfoot.red.data.api.MatchService;
import io.oldering.tvfoot.red.data.entity.search.Filter;
import io.reactivex.Observable;

import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_ERROR;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_LOADED;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_LOADING;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_ERROR;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_LOADED;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_LOADING;

public class MatchesRepository {
  final MatchService matchService;
  private int pageIndex = 0;

  public MatchesRepository(MatchService matchService) {
    this.matchService = matchService;
  }

  public Observable<MatchesViewState> loadFirstPage() {
    return matchService.findFuture(Filter.builder().limit(30).offset(0).build())
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

  public Observable<MatchesViewState> loadNextPage() {
    return matchService.findFuture(Filter.builder().limit(30).offset(30 * ++pageIndex).build())
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
