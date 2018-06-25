package com.benoitquenaudon.tvfoot.red.app.data.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter = true)
data class Match(
    val id: String,
    val label: String?,
    @Json(name = "start-at") val startAt: Date,
    @Json(name = "matchday") val matchDay: String?,
    @Json(name = "home-team") val homeTeam: Team,
    @Json(name = "away-team") val awayTeam: Team,
    val broadcasters: List<Broadcaster>?,
    val place: String?,
    val competition: Competition,
    val postponed: Boolean,
    val tags: List<String>?
) {

  companion object {
    val MATCH_ID = "MATCH_ID"
  }
}
