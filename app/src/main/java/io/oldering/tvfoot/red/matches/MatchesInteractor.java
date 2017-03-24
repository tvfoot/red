package io.oldering.tvfoot.red.matches;

import io.oldering.tvfoot.red.data.api.TvfootService;
import io.oldering.tvfoot.red.data.entity.search.Filter;
import io.oldering.tvfoot.red.matches.displayable.MatchRowDisplayable;
import io.reactivex.Observable;
import javax.inject.Inject;
import timber.log.Timber;

import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_ERROR;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_LOADED;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_LOADING;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_ERROR;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_LOADED;
import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.NEXT_PAGE_LOADING;

public class MatchesInteractor {
  private final TvfootService tvfootService;
  private int matchPerPage = 30;

  @Inject public MatchesInteractor(TvfootService tvfootService) {
    this.tvfootService = tvfootService;
  }

  public Observable<MatchesViewState> loadFirstPage() {
    return tvfootService.findFuture(Filter.builder().limit(matchPerPage).offset(0).build())
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
    return tvfootService.findFuture(
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
