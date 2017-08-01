package com.benoitquenaudon.tvfoot.red.app.data.entity

import com.squareup.moshi.Json
import java.util.Date

data class Match(
    val id: String,
    val label: String?,
    val `start-at`: Date,
    @Json(name = "matchday") val matchDay: String?,
    @Json(name = "home-team") val homeTeam: Team,
    @Json(name = "away-team") val awayTeam: Team,
    val broadcasters: List<Broadcaster>?,
    val place: String?,
    val competition: Competition,
    val postponed: Boolean
) {
  val startAt: Date
    get() {
      return `start-at`
    }

  companion object Constant {
    val MATCH_ID = "MATCH_ID"
  }
}
