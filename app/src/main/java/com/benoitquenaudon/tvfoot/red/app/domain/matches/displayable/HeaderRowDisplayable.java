package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable;

import com.benoitquenaudon.tvfoot.red.R;
import com.benoitquenaudon.tvfoot.red.util.DateUtils;
import com.google.auto.value.AutoValue;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import timber.log.Timber;

@AutoValue public abstract class HeaderRowDisplayable implements MatchesItemDisplayable {
  static SimpleDateFormat headerKeyDateFormat =
      new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
  static SimpleDateFormat dayOfWeekDateFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
  static SimpleDateFormat dayDateFormat = new SimpleDateFormat("EEEE d", Locale.getDefault());
  static SimpleDateFormat monthDateFormat =
      new SimpleDateFormat("EEEE, dd MMMM", Locale.getDefault());
  static SimpleDateFormat yearDateFormat =
      new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());

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
    if (DateUtils.isToday(date.getTime())) {
      dangerResId = R.string.matches_row_header_danger_today;
      displayedDate = dayOfWeekDateFormat.format(date);
      return new AutoValue_HeaderRowDisplayable(dangerResId, true, displayedDate);
    }

    if (DateUtils.isTomorrow(date.getTime())) {
      dangerResId = R.string.matches_row_header_danger_tomorrow;
      displayedDate = dayOfWeekDateFormat.format(date);
      return new AutoValue_HeaderRowDisplayable(dangerResId, true, displayedDate);
    }

    if (DateUtils.isThisWeek(date.getTime())) {
      displayedDate = dayDateFormat.format(date);
    } else if (DateUtils.isThisYear(date.getTime())) {
      displayedDate = monthDateFormat.format(date);
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
