package com.benoitquenaudon.tvfoot.red.app.data.source

import com.benoitquenaudon.tvfoot.red.api.TvfootService
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Tag
import com.benoitquenaudon.tvfoot.red.app.data.entity.search.Filter
import com.benoitquenaudon.tvfoot.red.app.data.entity.search.TagsFilter
import com.benoitquenaudon.tvfoot.red.app.data.entity.search.Where
import com.benoitquenaudon.tvfoot.red.app.data.entity.search.Where.TeamCondition
import com.benoitquenaudon.tvfoot.red.util.TeamCode
import io.reactivex.Single
import javax.inject.Inject

class MatchesRepository @Inject constructor(
    private val tvfootService: TvfootService
) : BaseMatchesRepository {
  override fun loadPage(pageIndex: Int): Single<List<Match>> =
      tvfootService.getMatches(Filter(limit = MATCH_PER_PAGE, offset = MATCH_PER_PAGE * pageIndex))

  override fun loadTags(): Single<List<Tag>> = tvfootService.getTags(TagsFilter())

  override fun searchTeamMatches(vararg codes: TeamCode): Single<List<Match>> =
      tvfootService.getMatches(
          Filter(
              limit = TEAM_SEARCH_LIMIT,
              offset = 0,
              where = Where(teams = codes.map(::TeamCondition))
          )
      )

  companion object Constant {
    private val MATCH_PER_PAGE = 300
    private val TEAM_SEARCH_LIMIT = 1500
  }
}
