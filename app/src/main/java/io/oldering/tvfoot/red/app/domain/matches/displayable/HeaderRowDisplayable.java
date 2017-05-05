package io.oldering.tvfoot.red.app.domain.matches.displayable;

import com.google.auto.value.AutoValue;
import io.oldering.tvfoot.red.R;
import io.oldering.tvfoot.red.util.DateUtils;
import java.text.ParseException;
import java.util.Date;
import timber.log.Timber;

import static io.oldering.tvfoot.red.app.common.TimeConstants.ONE_DAY_IN_MILLIS;

@AutoValue public abstract class HeaderRowDisplayable implements MatchesItemDisplayable {
  public abstract int dangerResId();

  public abstract boolean hasDanger();

  public abstract String displayedDate();

  public static HeaderRowDisplayable create(String headerKey) {
    int dangerResId = -1;
    String displayedDate;

    Date date;
    try {
      date = MatchRowDisplayable.mediumDateFormat.parse(headerKey);
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
