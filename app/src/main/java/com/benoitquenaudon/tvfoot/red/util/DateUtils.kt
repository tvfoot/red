package com.benoitquenaudon.tvfoot.red.util

import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR
import java.util.concurrent.TimeUnit

fun Long.isToday(nowCalendar: Calendar): Boolean {
  val targetCalendar = nowCalendar.clone() as Calendar
  targetCalendar.timeInMillis = this

  return nowCalendar.get(YEAR) == targetCalendar.get(YEAR)
      && nowCalendar.get(MONTH) == targetCalendar.get(MONTH)
      && nowCalendar.get(DAY_OF_MONTH) == targetCalendar.get(DAY_OF_MONTH)
}

fun Long.isTomorrow(nowCalendar: Calendar): Boolean {
  return (this - TimeUnit.DAYS.toMillis(1)).isToday(nowCalendar)
}

fun Long.isCurrentYear(nowCalendar: Calendar): Boolean {
  val targetCalendar = nowCalendar.clone() as Calendar
  targetCalendar.timeInMillis = this

  return nowCalendar.get(YEAR) == targetCalendar.get(YEAR)
}