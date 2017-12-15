package com.benoitquenaudon.tvfoot.red.app.data.entity.search

import com.benoitquenaudon.tvfoot.red.util.TeamCode
import com.squareup.moshi.formatIso8601
import java.util.Date
import java.util.concurrent.TimeUnit

data class Where(
    private val startAt: Long,
    private val onlyBroadcasted: Boolean = false,
    private val teams: List<TeamCondition> = emptyList()
) {
  override fun toString(): String {
    val buffer = mutableListOf<String>()

    buffer.add(""""start-at":{"gte":"${Date(startAt).formatIso8601()}"}""")
    buffer.add(""""deleted":{"neq":1}""")
    if (onlyBroadcasted) buffer.add(""""broadcasters":{"gt":[]}""")
    if (teams.isNotEmpty()) buffer.add(""""or":[${teams.joinToString(",")}]""")

    return buffer.joinToString(prefix = "{", separator = ",", postfix = "}")
  }

  data class TeamCondition(
      private val code: TeamCode
  ) {
    override fun toString() =
        """{"home-team.code":"$code"},{"away-team.code":"$code"}"""
  }
}
