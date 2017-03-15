package io.oldering.tvfoot.red.matches;

import io.reactivex.Observable;

public interface MatchesView {
  Observable<Boolean> loadFirstPageIntent();

  Observable<Boolean> loadNextPageIntent();

  //Observable<Boolean> pullToRefreshIntent();

  void render(MatchesViewState viewState);
}
