package com.benoitquenaudon.tvfoot.red.app.domain.matches.state

import com.benoitquenaudon.tvfoot.red.api.TvfootService
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.search.Filter
import io.reactivex.Single
import javax.inject.Inject

class MatchesService @Inject constructor(private val tvfootService: TvfootService) {
  fun loadPage(pageIndex: Int): Single<List<Match>> {
    return tvfootService.findFuture(
        Filter.builder().limit(MATCH_PER_PAGE).offset(MATCH_PER_PAGE * pageIndex).build())
  }

  companion object Constant {
    private val MATCH_PER_PAGE = 30
  }
}
