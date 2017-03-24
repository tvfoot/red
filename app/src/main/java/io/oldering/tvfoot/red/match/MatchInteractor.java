package io.oldering.tvfoot.red.match;

import io.oldering.tvfoot.red.data.api.TvfootService;
import io.reactivex.Observable;
import javax.inject.Inject;

public class MatchInteractor {
  private final TvfootService tvfootService;

  @Inject public MatchInteractor(TvfootService tvfootService) {
    this.tvfootService = tvfootService;
  }

  public Observable<MatchViewState> loadMatch() {

  }
}
