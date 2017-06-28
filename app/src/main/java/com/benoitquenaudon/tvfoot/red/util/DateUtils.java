package com.benoitquenaudon.tvfoot.red.util;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

public class DateUtils {
  public static boolean isToday(Calendar nowCalendar, long time) {
    Calendar targetCalendar = (Calendar) nowCalendar.clone();
    targetCalendar.setTimeInMillis(time);

    return nowCalendar.get(YEAR) == targetCalendar.get(YEAR)
        && nowCalendar.get(MONTH) == targetCalendar.get(MONTH)
        && nowCalendar.get(DAY_OF_MONTH) == targetCalendar.get(DAY_OF_MONTH);
  }

  public static boolean isTomorrow(Calendar nowCalendar, long time) {
    return isToday(nowCalendar, time - TimeUnit.DAYS.toMillis(1));
  }

  public static boolean isCurrentYear(Calendar nowCalendar, long time) {
    Calendar targetCalendar = (Calendar) nowCalendar.clone();
    targetCalendar.setTimeInMillis(time);

    return nowCalendar.get(YEAR) == targetCalendar.get(YEAR);
  }
}
