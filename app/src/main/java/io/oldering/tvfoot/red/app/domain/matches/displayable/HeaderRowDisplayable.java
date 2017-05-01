package io.oldering.tvfoot.red.app.domain.matches.displayable;

import com.google.auto.value.AutoValue;
import io.oldering.tvfoot.red.util.DateUtils;
import java.text.ParseException;
import java.util.Date;
import timber.log.Timber;

import static io.oldering.tvfoot.red.app.common.TimeConstants.ONE_DAY_IN_MILLIS;

@AutoValue public abstract class HeaderRowDisplayable implements MatchesItemDisplayable {
  public abstract String danger();

  public abstract String displayedDate();

  public static HeaderRowDisplayable create(String headerKey) {
    String danger = "";
    String displayedDate;

    Date date;
    try {
      date = MatchRowDisplayable.mediumDateFormat.parse(headerKey);
    } catch (ParseException e) {
      Timber.e(e);
      throw new UnsupportedOperationException("What is this date anyway? " + headerKey);
    }
    if (DateUtils.isToday(date.getTime())) {
      danger = "AUJOURD'HUI";
    } else if (DateUtils.isToday(date.getTime() - ONE_DAY_IN_MILLIS)) {
      danger = "DEMAIN";
    }
    displayedDate = headerKey;
    return new AutoValue_HeaderRowDisplayable(danger, displayedDate);
  }

  @Override public boolean isSameAs(MatchesItemDisplayable newItem) {
    return newItem instanceof HeaderRowDisplayable && this.displayedDate()
        .equals(((HeaderRowDisplayable) newItem).displayedDate());
  }
}
