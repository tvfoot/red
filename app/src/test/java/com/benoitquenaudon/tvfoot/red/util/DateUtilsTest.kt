package com.benoitquenaudon.tvfoot.red.util

import org.junit.Assert
import org.junit.Test
import java.util.Calendar

class DateUtilsTest {
  @Test fun isToday() {
    Assert.assertTrue(todayCalendar.timeInMillis.isToday(todayCalendar))

    Assert.assertFalse(yesterdayCalendar(todayCalendar).timeInMillis.isToday(todayCalendar))

    Assert.assertFalse(tomorrowCalendar(todayCalendar).timeInMillis.isToday(todayCalendar))
  }

  @Test fun isTomorrow() {
    Assert.assertFalse(todayCalendar.timeInMillis.isTomorrow(todayCalendar))

    Assert.assertFalse(yesterdayCalendar(todayCalendar).timeInMillis.isTomorrow(todayCalendar))

    Assert.assertTrue(tomorrowCalendar(todayCalendar).timeInMillis.isTomorrow(todayCalendar))
  }

  @Test fun isCurrentYear() {
    Assert.assertTrue(todayCalendar.timeInMillis.isCurrentYear(todayCalendar))

    Assert.assertTrue(endOfYearCalendar(todayCalendar).timeInMillis.isCurrentYear(todayCalendar))

    Assert.assertFalse(
        beginningOfNextYearCalendar(todayCalendar).timeInMillis.isCurrentYear(todayCalendar))
  }

  companion object {
    val todayCalendar: Calendar = Calendar.getInstance()

    fun yesterdayCalendar(todayCalendar: Calendar): Calendar {
      val yesterdayCalendar = todayCalendar.clone() as Calendar
      yesterdayCalendar.add(Calendar.DAY_OF_YEAR, -1)
      yesterdayCalendar.set(Calendar.HOUR_OF_DAY, 23)
      yesterdayCalendar.set(Calendar.MINUTE, 59)
      yesterdayCalendar.set(Calendar.SECOND, 59)
      yesterdayCalendar.set(Calendar.MILLISECOND, 999)
      return yesterdayCalendar
    }

    fun tomorrowCalendar(todayCalendar: Calendar): Calendar {
      val tomorrowCalendar = todayCalendar.clone() as Calendar
      tomorrowCalendar.add(Calendar.DAY_OF_YEAR, 1)
      tomorrowCalendar.set(Calendar.HOUR_OF_DAY, 0)
      tomorrowCalendar.set(Calendar.MINUTE, 0)
      tomorrowCalendar.set(Calendar.SECOND, 0)
      tomorrowCalendar.set(Calendar.MILLISECOND, 0)
      return tomorrowCalendar
    }

    fun endOfYearCalendar(todayCalendar: Calendar): Calendar {
      val endOfYearCalendar = todayCalendar.clone() as Calendar
      endOfYearCalendar.set(Calendar.DAY_OF_YEAR, 364)
      endOfYearCalendar.set(Calendar.HOUR_OF_DAY, 23)
      endOfYearCalendar.set(Calendar.MINUTE, 59)
      endOfYearCalendar.set(Calendar.SECOND, 59)
      endOfYearCalendar.set(Calendar.MILLISECOND, 999)
      return endOfYearCalendar
    }

    fun beginningOfNextYearCalendar(todayCalendar: Calendar): Calendar {
      val beginningOfNextYearCalendar = todayCalendar.clone() as Calendar
      beginningOfNextYearCalendar.add(Calendar.YEAR, 1)
      beginningOfNextYearCalendar.set(Calendar.DAY_OF_YEAR, 1)
      beginningOfNextYearCalendar.set(Calendar.HOUR_OF_DAY, 0)
      beginningOfNextYearCalendar.set(Calendar.MINUTE, 0)
      beginningOfNextYearCalendar.set(Calendar.SECOND, 0)
      beginningOfNextYearCalendar.set(Calendar.MILLISECOND, 0)
      return beginningOfNextYearCalendar
    }
  }
}