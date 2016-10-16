package io.oldering.tvfoot.red.viewmodel;

import android.text.format.DateUtils;

import com.google.auto.value.AutoValue;

import java.text.ParseException;
import java.util.Date;

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
            throw new UnsupportedOperationException("What is this date anyway?");
        }
        if (DateUtils.isToday(date.getTime())) {
            danger = "AUJOURD'HUI";
        }
        displayedDate = headerKey;
        return new AutoValue_DayHeaderViewModel(danger, displayedDate);
    }
}
