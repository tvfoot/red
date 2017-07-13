package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable;

import com.benoitquenaudon.tvfoot.red.R;
import com.benoitquenaudon.tvfoot.red.util.DateUtilsKt;
import com.google.auto.value.AutoValue;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import kotlin.text.StringsKt;
import timber.log.Timber;

@AutoValue public abstract class HeaderRowDisplayable implements MatchesItemDisplayable {
  private static final ThreadLocal<DateFormat> headerKeyDateFormat = new ThreadLocal<DateFormat>() {
    @Override protected DateFormat initialValue() {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
      format.setTimeZone(TimeZone.getDefault());
      return format;
    }
  };
  private static final ThreadLocal<DateFormat> monthDateFormat = new ThreadLocal<DateFormat>() {
    @Override protected DateFormat initialValue() {
      SimpleDateFormat format = new SimpleDateFormat("EEEE, d MMMM", Locale.getDefault());
      format.setTimeZone(TimeZone.getDefault());
      return format;
    }
  };
  private static final ThreadLocal<DateFormat> yearDateFormat = new ThreadLocal<DateFormat>() {
    @Override protected DateFormat initialValue() {
      SimpleDateFormat format = new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault());
      format.setTimeZone(TimeZone.getDefault());
      return format;
    }
  };

  public abstract int dangerResId();

  public abstract boolean hasDanger();

  public abstract String displayedDate();

  public static HeaderRowDisplayable create(String headerKey) {
    Date date;
    try {
      date = headerKeyDateFormat.get().parse(headerKey);
    } catch (ParseException e) {
      Timber.e(e);
      throw new UnsupportedOperationException("What is this date anyway? " + headerKey);
    }

    int dangerResId = -1;
    String displayedDate;
    Calendar nowCalendar = Calendar.getInstance();
    if (DateUtilsKt.isToday(date.getTime(), nowCalendar)) {
      dangerResId = R.string.matches_row_header_danger_today;
      displayedDate = StringsKt.capitalize(monthDateFormat.get().format(date));
      return new AutoValue_HeaderRowDisplayable(dangerResId, true, displayedDate);
    }

    if (DateUtilsKt.isTomorrow(date.getTime(), nowCalendar)) {
      dangerResId = R.string.matches_row_header_danger_tomorrow;
      displayedDate = StringsKt.capitalize(monthDateFormat.get().format(date));
      return new AutoValue_HeaderRowDisplayable(dangerResId, true, displayedDate);
    }

    if (DateUtilsKt.isCurrentYear(date.getTime(), nowCalendar)) {
      displayedDate = StringsKt.capitalize(monthDateFormat.get().format(date));
    } else {
      displayedDate = yearDateFormat.get().format(date);
    }

    return new AutoValue_HeaderRowDisplayable(dangerResId, false, displayedDate);
  }

  @Override public boolean isSameAs(MatchesItemDisplayable newItem) {
    return newItem instanceof HeaderRowDisplayable && this.displayedDate()
        .equals(((HeaderRowDisplayable) newItem).displayedDate());
  }
}
