package io.oldering.tvfoot.red.viewmodel;

import android.text.format.DateUtils;

import com.google.auto.value.AutoValue;

import java.text.ParseException;
import java.util.Date;

@AutoValue
public abstract class DayHeaderViewModel {
    private static final int ONE_SECOND = 1000;
    private static final int ONE_MINUTE = 60 * ONE_SECOND;
    private static final int ONE_HOUR = 60 * ONE_MINUTE;
    private static final long ONE_DAY = 24 * ONE_HOUR;

    public abstract String getDanger();

    public abstract String getDisplayedDate();

    public static DayHeaderViewModel create(String headerKey) {
        String danger = "";
        String displayedDate;

        Date date;
        try {
            date = MatchListViewModel.simpleDateFormat.parse(headerKey);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new UnsupportedOperationException("What is this date anyway? " + headerKey);
        }
        // TODO(benoit) refactor this so it does not depend on Android APIs
        // and DateUtils.isToday uses deprecated Time class
        if (DateUtils.isToday(date.getTime())) {
            danger = "AUJOURD'HUI";
        } else if (DateUtils.isToday(date.getTime() - ONE_DAY)) {
            danger = "DEMAIN";
        }
        displayedDate = headerKey;
        return new AutoValue_DayHeaderViewModel(danger, displayedDate);
    }
}
