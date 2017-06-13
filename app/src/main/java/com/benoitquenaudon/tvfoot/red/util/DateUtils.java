package com.benoitquenaudon.tvfoot.red.util;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONDAY;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.WEEK_OF_YEAR;
import static java.util.Calendar.YEAR;

public class DateUtils {

  public static boolean isToday(long time) {
    Calendar nowCalendar = Calendar.getInstance();

    Calendar targetCalendar = Calendar.getInstance();
    targetCalendar.setTimeInMillis(time);

    return nowCalendar.get(YEAR) == targetCalendar.get(YEAR)
        && nowCalendar.get(MONTH) == targetCalendar.get(MONTH)
        && nowCalendar.get(DAY_OF_MONTH) == targetCalendar.get(DAY_OF_MONTH);
  }

  public static boolean isTomorrow(long time) {
    return isToday(time - TimeUnit.DAYS.toMillis(1));
  }

  public static boolean isThisYear(long time) {
    Calendar nowCalendar = Calendar.getInstance();

    Calendar targetCalendar = Calendar.getInstance();
    targetCalendar.setTimeInMillis(time);

    return nowCalendar.get(YEAR) == targetCalendar.get(YEAR);
  }
}
