package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable

import com.benoitquenaudon.tvfoot.red.R
import com.benoitquenaudon.tvfoot.red.util.formatter
import com.benoitquenaudon.tvfoot.red.util.isCurrentYear
import com.benoitquenaudon.tvfoot.red.util.isToday
import com.benoitquenaudon.tvfoot.red.util.isTomorrow
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.util.Calendar
import java.util.Date

@Suppress("DataClassPrivateConstructor")
data class HeaderRowDisplayable private constructor(
    /**
     * Used for sticky headers
     */
    val id: Int,
    val dangerResId: Int,
    val hasDanger: Boolean,
    val displayedDate: String
) : MatchesItemDisplayable {
  override fun isSameAs(other: MatchesItemDisplayable): Boolean {
    return other is HeaderRowDisplayable && this.displayedDate == other.displayedDate
  }

  companion object Factory {
    private val headerKeyDateFormat: DateFormat by formatter("yyyy-MM-dd")
    private val monthDateFormat: DateFormat by formatter("EEEE, d MMMM")
    private val yearDateFormat: DateFormat by formatter("EEEE, d MMMM yyyy")

    fun create(headerKey: String): HeaderRowDisplayable {
      val date: Date
      try {
        date = headerKeyDateFormat.parse(headerKey)
      } catch (e: ParseException) {
        Timber.e(e)
        throw UnsupportedOperationException("What is this date anyway? " + headerKey)
      }

      // yyyy-MM-dd without the '-' to int
      val id = headerKeyDateFormat.format(date).filter { it != '-' }.toInt()

      var dangerResId = -1
      val displayedDate: String
      val nowCalendar = Calendar.getInstance()
      if (date.time.isToday(nowCalendar)) {
        dangerResId = R.string.matches_row_header_danger_today
        displayedDate = monthDateFormat.format(date).capitalize()
        return HeaderRowDisplayable(id, dangerResId, true, displayedDate)
      }

      if (date.time.isTomorrow(nowCalendar)) {
        dangerResId = R.string.matches_row_header_danger_tomorrow
        displayedDate = monthDateFormat.format(date).capitalize()
        return HeaderRowDisplayable(id, dangerResId, true, displayedDate)
      }

      displayedDate = if (date.time.isCurrentYear(nowCalendar)) {
        monthDateFormat.format(date).capitalize()
      } else {
        yearDateFormat.format(date)
      }

      return HeaderRowDisplayable(id, dangerResId, false, displayedDate)
    }
  }
}
