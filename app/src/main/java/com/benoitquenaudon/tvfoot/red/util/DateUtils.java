package com.benoitquenaudon.tvfoot.red.util;

import java.util.Calendar;

public class DateUtils {

  public static boolean isToday(long time) {
    Calendar nowCalendar = Calendar.getInstance();

    Calendar targetCalendar = Calendar.getInstance();
    targetCalendar.setTimeInMillis(time);

    return nowCalendar.get(Calendar.YEAR) == targetCalendar.get(Calendar.YEAR)
        && nowCalendar.get(Calendar.MONTH) == targetCalendar.get(Calendar.MONTH)
        && nowCalendar.get(Calendar.DAY_OF_MONTH) == targetCalendar.get(Calendar.DAY_OF_MONTH);
  }
}
