package io.oldering.tvfoot.red.matches;

import io.oldering.tvfoot.red.data.api.TvfootService;
import io.oldering.tvfoot.red.data.entity.Match;
import io.oldering.tvfoot.red.data.entity.search.Filter;
import io.reactivex.Single;
import java.util.List;
import javax.inject.Inject;

public class MatchesService {
  private final TvfootService tvfootService;
  private int MATCH_PER_PAGE = 30;

  @Inject public MatchesService(TvfootService tvfootService) {
    this.tvfootService = tvfootService;
  }

  public Single<List<Match>> loadFirstPage() {
    return tvfootService.findFuture(Filter.builder().limit(MATCH_PER_PAGE).offset(0).build());
  }

  public Single<List<Match>> loadNextPage(int currentPage) {
    return tvfootService.findFuture(
        Filter.builder().limit(MATCH_PER_PAGE).offset(MATCH_PER_PAGE * currentPage).build());
  }
}
