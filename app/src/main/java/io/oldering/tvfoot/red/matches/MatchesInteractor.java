package io.oldering.tvfoot.red.matches;

import io.oldering.tvfoot.red.data.api.MatchService;
import io.oldering.tvfoot.red.data.model.search.Filter;
import io.reactivex.Observable;

import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_ERROR;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_LOADED;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_LOADING;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_ERROR;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_LOADED;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_LOADING;

public class MatchesInteractor {
  final MatchService matchService;
  private int pageIndex = 0;

  public MatchesInteractor(MatchService matchService) {
    this.matchService = matchService;
  }

  public Observable<MatchesViewState> loadFirstPage() {
    return matchService.findFuture(Filter.create(30, 0))
        .toObservable()
        .map(matches -> MatchesViewState.builder()
            .setMatches(matches)
            .setStatus(FIRST_PAGE_LOADED)
            .build())
        .startWith(MatchesViewState.builder().setStatus(FIRST_PAGE_LOADING).build())
        .onErrorReturn(error -> MatchesViewState.builder()
            .setFirstPageError(error)
            .setStatus(FIRST_PAGE_ERROR)
            .build());
  }

  public Observable<MatchesViewState> loadNextPage() {
    return matchService.findFuture(Filter.create(30, ++pageIndex))
        .toObservable()
        .map(matches -> MatchesViewState.builder()
            .setMatches(matches)
            .setStatus(NEXT_PAGE_LOADED)
            .build())
        .startWith(MatchesViewState.builder().setStatus(NEXT_PAGE_LOADING).build())
        .onErrorReturn(error -> MatchesViewState.builder()
            .setFirstPageError(error)
            .setStatus(NEXT_PAGE_ERROR)
            .build());
  }
}
