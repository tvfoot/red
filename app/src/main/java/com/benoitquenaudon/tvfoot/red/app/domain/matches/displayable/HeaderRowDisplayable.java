package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable;

import com.benoitquenaudon.tvfoot.red.R;
import com.benoitquenaudon.tvfoot.red.util.DateUtilsKt;
import com.google.auto.value.AutoValue;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import kotlin.text.StringsKt;
import timber.log.Timber;

@AutoValue public abstract class HeaderRowDisplayable implements MatchesItemDisplayable {
  private static SimpleDateFormat headerKeyDateFormat =
      new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
  private static SimpleDateFormat monthDateFormat =
      new SimpleDateFormat("EEEE, d MMMM", Locale.getDefault());
  private static SimpleDateFormat yearDateFormat =
      new SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault());

  public abstract int dangerResId();

  public abstract boolean hasDanger();

  public abstract String displayedDate();

  public static HeaderRowDisplayable create(String headerKey) {
    Date date;
    try {
      date = headerKeyDateFormat.parse(headerKey);
    } catch (ParseException e) {
      Timber.e(e);
      throw new UnsupportedOperationException("What is this date anyway? " + headerKey);
    }

    int dangerResId = -1;
    String displayedDate;
    Calendar nowCalendar = Calendar.getInstance();
    if (DateUtilsKt.isToday(date.getTime(), nowCalendar)) {
      dangerResId = R.string.matches_row_header_danger_today;
      displayedDate = StringsKt.capitalize(monthDateFormat.format(date));
      return new AutoValue_HeaderRowDisplayable(dangerResId, true, displayedDate);
    }

    if (DateUtilsKt.isTomorrow(date.getTime(), nowCalendar)) {
      dangerResId = R.string.matches_row_header_danger_tomorrow;
      displayedDate = StringsKt.capitalize(monthDateFormat.format(date));
      return new AutoValue_HeaderRowDisplayable(dangerResId, true, displayedDate);
    }

    if (DateUtilsKt.isCurrentYear(date.getTime(), nowCalendar)) {
      displayedDate = StringsKt.capitalize(monthDateFormat.format(date));
    } else {
      displayedDate = yearDateFormat.format(date);
    }

    return new AutoValue_HeaderRowDisplayable(dangerResId, false, displayedDate);
  }

  @Override public boolean isSameAs(MatchesItemDisplayable newItem) {
    return newItem instanceof HeaderRowDisplayable && this.displayedDate()
        .equals(((HeaderRowDisplayable) newItem).displayedDate());
  }
}
