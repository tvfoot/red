package io.oldering.tvfoot.red.matches;

import io.oldering.tvfoot.red.data.model.Match;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

import static io.oldering.tvfoot.red.matches.MatchesViewState.Status.FIRST_PAGE_LOADING;

public class MatchesPresenter {
  private final MatchesView view;
  private final MatchesInteractor interactor;

  public MatchesPresenter(MatchesView view, MatchesInteractor interactor) {
    this.view = view;
    this.interactor = interactor;
  }

  void bindIntents() {
    Timber.d("binding intents");
    Observable<MatchesViewState> loadFirstPage = view.loadFirstPageIntent()
        .doOnNext(ignored -> Timber.d("intent: load first page"))
        .flatMap(ignored -> interactor.loadFirstPage().subscribeOn(Schedulers.io()));

    Observable<MatchesViewState> loadNextPage = view.loadNextPageIntent()
        .doOnNext(ignored -> Timber.d("intent: load next page"))
        .flatMap(ignored -> interactor.loadNextPage().subscribeOn(Schedulers.io()));
    //Observable<MatchesViewState> pullToRefresh = view.pullToRefreshIntent()
    //    .doOnNext(ignored -> Timber.d("intent: pull to refresh"))
    //    .flatMap(ignored -> interactor.pullToRefresh().subscribeOn(Schedulers.io()));

    Observable<MatchesViewState> allIntents = loadFirstPage.mergeWith(loadNextPage);
    MatchesViewState initialViewState =
        MatchesViewState.builder().setStatus(FIRST_PAGE_LOADING).build();
    allIntents.scan(initialViewState, this::viewStateReducer)
        .doOnNext(ignored -> Timber.d("onnext yo"))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(view::render);
  }

  private MatchesViewState viewStateReducer(MatchesViewState previousState,
      MatchesViewState changes) {
    switch (changes.status()) {
      case FIRST_PAGE_LOADING:
        return previousState.toBuilder()
            .setFirstPageLoading(true)
            .setFirstPageError(null)
            .setStatus(changes.status())
            .build();
      case FIRST_PAGE_ERROR:
        return previousState.toBuilder()
            .setFirstPageLoading(false)
            .setFirstPageError(changes.firstPageError())
            .setStatus(changes.status())
            .build();
      case FIRST_PAGE_LOADED:
        return previousState.toBuilder()
            .setFirstPageLoading(false)
            .setFirstPageError(null)
            .setMatches(changes.matches())
            .setStatus(changes.status())
            .build();
      case NEXT_PAGE_LOADING:
        return previousState.toBuilder()
            .setNextPageLoading(true)
            .setNextPageError(null)
            .setStatus(changes.status())
            .build();
      case NEXT_PAGE_ERROR:
        return previousState.toBuilder()
            .setNextPageLoading(false)
            .setNextPageError(changes.nextPageError())
            .setStatus(changes.status())
            .build();
      case NEXT_PAGE_LOADED:
        List<Match> matches = new ArrayList<>();
        matches.addAll(previousState.matches());
        matches.addAll(changes.matches());

        return previousState.toBuilder()
            .setNextPageLoading(false)
            .setNextPageError(null)
            .setMatches(matches)
            .setStatus(changes.status())
            .build();
      case PULL_TO_REFRESH_LOADING:
        return previousState.toBuilder()
            .setPullToRefreshLoading(true)
            .setPullToRefreshError(null)
            .setStatus(changes.status())
            .build();
      case PULL_TO_REFRESH_ERROR:
        return previousState.toBuilder()
            .setPullToRefreshLoading(false)
            .setPullToRefreshError(changes.pullToRefreshError())
            .setStatus(changes.status())
            .build();
      case PULL_TO_REFRESH_LOADED:
        matches = new ArrayList<>();
        matches.addAll(changes.matches());
        matches.addAll(previousState.matches());

        return previousState.toBuilder()
            .setPullToRefreshLoading(false)
            .setPullToRefreshError(null)
            .setMatches(matches)
            .setStatus(changes.status())
            .build();
      default:
        throw new IllegalArgumentException("Don't know this one " + changes);
    }
  }
}
