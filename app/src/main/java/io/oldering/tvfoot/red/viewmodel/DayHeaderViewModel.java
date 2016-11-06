package io.oldering.tvfoot.red.viewmodel;

import android.text.format.DateUtils;

import com.google.auto.value.AutoValue;

import java.text.ParseException;
import java.util.Date;

import static io.oldering.tvfoot.red.util.TimeConstants.ONE_DAY_IN_MILLIS;

@AutoValue
public abstract class DayHeaderViewModel {

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
        // TODO(benoit) refactor this so it does not depend on Android APIs so I can test it
        // and DateUtils.isToday uses deprecated Time class
        if (DateUtils.isToday(date.getTime())) {
            danger = "AUJOURD'HUI";
        } else if (DateUtils.isToday(date.getTime() - ONE_DAY_IN_MILLIS)) {
            danger = "DEMAIN";
        }
        displayedDate = headerKey;
        return new AutoValue_DayHeaderViewModel(danger, displayedDate);
    }
}
