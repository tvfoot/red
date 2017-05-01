package io.oldering.tvfoot.red.app.domain.matches.state;

import io.oldering.tvfoot.red.api.TvfootService;
import io.oldering.tvfoot.red.app.data.entity.Match;
import io.oldering.tvfoot.red.app.data.entity.search.Filter;
import io.reactivex.Single;
import java.util.List;
import javax.inject.Inject;

public class MatchesService {
  private final TvfootService tvfootService;
  private static final int MATCH_PER_PAGE = 30;

  @Inject MatchesService(TvfootService tvfootService) {
    this.tvfootService = tvfootService;
  }

  Single<List<Match>> loadFirstPage() {
    return tvfootService.findFuture(Filter.builder().limit(MATCH_PER_PAGE).offset(0).build());
  }

  Single<List<Match>> loadNextPage(int pageIndex) {
    return tvfootService.findFuture(
        Filter.builder().limit(MATCH_PER_PAGE).offset(MATCH_PER_PAGE * pageIndex).build());
  }
}
