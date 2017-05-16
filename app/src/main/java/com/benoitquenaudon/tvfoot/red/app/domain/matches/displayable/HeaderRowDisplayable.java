package com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable;

import com.benoitquenaudon.tvfoot.red.R;
import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchDisplayable;
import com.benoitquenaudon.tvfoot.red.util.DateUtils;
import com.google.auto.value.AutoValue;
import java.text.ParseException;
import java.util.Date;
import timber.log.Timber;

import static com.benoitquenaudon.tvfoot.red.app.common.TimeConstants.ONE_DAY_IN_MILLIS;

@AutoValue public abstract class HeaderRowDisplayable implements MatchesItemDisplayable {
  public abstract int dangerResId();

  public abstract boolean hasDanger();

  public abstract String displayedDate();

  public static HeaderRowDisplayable create(String headerKey) {
    int dangerResId = -1;
    String displayedDate;

    Date date;
    try {
      date = MatchDisplayable.mediumDateFormat.parse(headerKey);
    } catch (ParseException e) {
      Timber.e(e);
      throw new UnsupportedOperationException("What is this date anyway? " + headerKey);
    }
    if (DateUtils.isToday(date.getTime())) {
      dangerResId = R.string.matches_row_header_danger_today;
    } else if (DateUtils.isToday(date.getTime() - ONE_DAY_IN_MILLIS)) {
      dangerResId = R.string.matches_row_header_danger_tomorrow;
    }
    displayedDate = headerKey;
    return new AutoValue_HeaderRowDisplayable(dangerResId, dangerResId > 0, displayedDate);
  }

  @Override public boolean isSameAs(MatchesItemDisplayable newItem) {
    return newItem instanceof HeaderRowDisplayable && this.displayedDate()
        .equals(((HeaderRowDisplayable) newItem).displayedDate());
  }
}
