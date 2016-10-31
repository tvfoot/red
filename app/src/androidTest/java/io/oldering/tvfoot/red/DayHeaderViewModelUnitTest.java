package io.oldering.tvfoot.red;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.oldering.tvfoot.red.viewmodel.DayHeaderViewModel;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DayHeaderViewModelUnitTest {
    static final int ONE_DAY = 1000 * 60 * 60 * 24;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);

    @Before
    public void before() {
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
    }

    @Test
    public void create_Aujourdhui() {
        String headerKey = simpleDateFormat.format(new Date(System.currentTimeMillis()));
        DayHeaderViewModel dayHeaderVM = DayHeaderViewModel.create(headerKey);

        assertEquals(dayHeaderVM.getDanger(), "AUJOURD'HUI");
        assertEquals(dayHeaderVM.getDisplayedDate(), headerKey);
    }

    @Test
    public void create_Hier() {
        String headerKey = simpleDateFormat.format(new Date(System.currentTimeMillis() - ONE_DAY));
        DayHeaderViewModel dayHeaderVM = DayHeaderViewModel.create(headerKey);

        assertEquals(dayHeaderVM.getDanger(), "");
        assertEquals(dayHeaderVM.getDisplayedDate(), headerKey);
    }

    @Test
    public void create_Demain() {
        String headerKey = simpleDateFormat.format(new Date(System.currentTimeMillis() + ONE_DAY));
        DayHeaderViewModel dayHeaderVM = DayHeaderViewModel.create(headerKey);

        assertEquals(dayHeaderVM.getDanger(), "DEMAIN");
        assertEquals(dayHeaderVM.getDisplayedDate(), headerKey);
    }

    @Test
    public void create_ApresDemain() {
        String headerKey = simpleDateFormat.format(new Date(System.currentTimeMillis() + 2 * ONE_DAY));
        DayHeaderViewModel dayHeaderVM = DayHeaderViewModel.create(headerKey);

        assertEquals(dayHeaderVM.getDanger(), "");
        assertEquals(dayHeaderVM.getDisplayedDate(), headerKey);
    }
}
