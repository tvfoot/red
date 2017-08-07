package com.benoitquenaudon.tvfoot.red.app.domain.matches.state

import com.benoitquenaudon.tvfoot.red.api.TvfootService
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.search.Filter
import io.reactivex.Single
import javax.inject.Inject

class MatchesRepository @Inject constructor(
    private val tvfootService: TvfootService
) : BaseMatchesRepository {
  override fun loadPage(pageIndex: Int): Single<List<Match>> {
    return tvfootService.findFuture(
        Filter(limit = MATCH_PER_PAGE, offset = MATCH_PER_PAGE * pageIndex))
  }

  companion object Constant {
    private val MATCH_PER_PAGE = 30
  }
}
