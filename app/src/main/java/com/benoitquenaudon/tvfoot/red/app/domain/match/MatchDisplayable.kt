package com.benoitquenaudon.tvfoot.red.app.domain.match

import android.os.Parcelable
import com.benoitquenaudon.tvfoot.red.app.data.entity.Broadcaster
import com.benoitquenaudon.tvfoot.red.app.data.entity.Competition
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match
import com.benoitquenaudon.tvfoot.red.app.data.entity.Team
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.BroadcasterRowDisplayable
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchesItemDisplayable
import java.text.DateFormat
import java.text.SimpleDateFormat
import kotlinx.android.parcel.Parcelize
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

@Parcelize
data class MatchDisplayable(
    val headerKey: String,
    val startAt: Long,
    val startTime: String,
    val broadcasters: List<BroadcasterRowDisplayable>,
    val headline: String,
    val competition: String,
    val matchDay: String,
    val live: Boolean,
    val startTimeInText: String,
    val homeTeamLogoPath: String,
    val awayTeamLogoPath: String,
    val location: String?,
    val matchId: String
) : Parcelable, MatchesItemDisplayable {

  override fun isSameAs(other: MatchesItemDisplayable): Boolean {
    return other is MatchDisplayable && this.matchId == other.matchId
  }

  companion object {
    private val shortDateFormat = object : ThreadLocal<DateFormat>() {
      override fun initialValue(): DateFormat {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        format.timeZone = TimeZone.getDefault()
        return format
      }
    }
    private val mediumDateFormat = object : ThreadLocal<DateFormat>() {
      override fun initialValue(): DateFormat {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        format.timeZone = TimeZone.getDefault()
        return format
      }
    }
    private val fullTextDateFormat = object : ThreadLocal<DateFormat>() {
      override fun initialValue(): DateFormat {
        val format = SimpleDateFormat("EEEE dd MMMM yyyy HH'h'mm", Locale.getDefault())
        format.timeZone = TimeZone.getDefault()
        return format
      }
    }

    fun fromMatch(match: Match): MatchDisplayable {
      return MatchDisplayable(
          parseHeaderKey(match.startAt),
          match.startAt.time,
          parseStartTime(match.startAt),
          parseBroadcasters(match.broadcasters),
          parseHeadLine(match.homeTeam, match.awayTeam, match.label),
          parseCompetition(match.competition),
          parseMatchDay(match.label, match.matchDay),
          isMatchLive(match.startAt),
          parseStartTimeInText(match.startAt),
          match.homeTeam.logoPath,
          match.awayTeam.logoPath,
          parseLocation(match),
          match.id)
    }

    private fun parseHeaderKey(startAt: Date): String {
      return mediumDateFormat.get().format(startAt)
    }

    private fun parseStartTime(startAt: Date): String {
      return shortDateFormat.get().format(startAt)
    }

    private fun parseBroadcasters(
        broadcasters: List<Broadcaster>?
    ): List<BroadcasterRowDisplayable> =
        broadcasters
            ?.filter { it.name != null }
            ?.map { BroadcasterRowDisplayable(it.name!!, it.code) }
            ?: emptyList()

    private fun parseHeadLine(homeTeam: Team, awayTeam: Team, matchLabel: String?): String =
        if (homeTeam.isEmpty || awayTeam.isEmpty) {
          matchLabel.toString()
        } else {
          homeTeam.name.toString().toUpperCase() + " - " + awayTeam.name.toString().toUpperCase()
        }

    private fun parseCompetition(competition: Competition): String = competition.name

    private fun parseMatchDay(matchLabel: String?, matchDay: String?): String =
        if (matchLabel != null && !matchLabel.isEmpty()) {
          matchLabel
        } else if (matchDay != null && !matchDay.isEmpty()) {
          "J. $matchDay"
        } else {
          ""
        }

    private fun isMatchLive(startAt: Date): Boolean {
      val now = Calendar.getInstance().timeInMillis
      val startTimeInMillis = startAt.time
      return now >= startTimeInMillis && now <= startTimeInMillis + TimeUnit.MINUTES.toMillis(105)
    }

    private fun parseStartTimeInText(startAt: Date): String =
        fullTextDateFormat.get().format(startAt).capitalize()

    private fun parseLocation(match: Match): String? = match.place
  }
}
