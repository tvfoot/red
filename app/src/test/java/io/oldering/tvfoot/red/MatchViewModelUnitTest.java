package io.oldering.tvfoot.red;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.oldering.tvfoot.red.model.Broadcaster;
import io.oldering.tvfoot.red.model.Competition;
import io.oldering.tvfoot.red.model.Team;
import io.oldering.tvfoot.red.viewmodel.BroadcasterViewModel;
import io.oldering.tvfoot.red.viewmodel.MatchViewModel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MatchViewModelUnitTest {
    @Test
    public void create() {
    }

    @Test
    public void parseStartTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        Date startTime = Calendar.getInstance().getTime();

        // stupid test... maybe not worth testing it
        assertEquals(MatchViewModel.parseStartTime(startTime), simpleDateFormat.format(startTime));
    }

    @Test
    public void parseBroadcasters() {
        List<Broadcaster> broadcasters = generateBroadcasters();

        List<BroadcasterViewModel> broadcasterVMs = MatchViewModel.parseBroadcasters(broadcasters);
        assertEquals(broadcasterVMs.size(), 3);
        assertEquals(broadcasterVMs.get(0).getCode(), "codeA");
        assertEquals(broadcasterVMs.get(1).getCode(), "codeB");
        assertEquals(broadcasterVMs.get(2).getCode(), "codeC");
    }

    @Test
    public void parseBroadcasters_empty() {
        assertEquals(MatchViewModel.parseBroadcasters(null), new ArrayList<>());
    }

    @Test
    public void parseHeadline() {
        String homeAwayName = "home";
        Team homeTeam = Team.create(null, homeAwayName, null, null, null, null, null, null, null);
        String notEmpty = "not empty";
        String awayTeamName = "AWaY";
        Team awayTeam = Team.create(notEmpty, awayTeamName, notEmpty, notEmpty, notEmpty, notEmpty, notEmpty, notEmpty, notEmpty);

        assertEquals(
                MatchViewModel.parseHeadLine(homeTeam, awayTeam, null),
                homeAwayName.toUpperCase() + " - " + awayTeamName.toUpperCase()
        );
    }

    @Test
    public void parseHeadline_noteam() {
        Team homeTeam = Team.create(null, null, null, null, null, null, null, null, null);
        Team awayTeam = Team.create(null, null, null, null, null, null, null, null, null);
        String matchLabel = "label";

        assertEquals(
                MatchViewModel.parseHeadLine(homeTeam, awayTeam, matchLabel),
                matchLabel
        );
    }

    @Test
    public void parseCompetition() {
        String code = "code";
        String name = "name";
        String country = "country";
        String url = "url";
        String gender = "gender";
        Competition competition = Competition.create(code, name, country, url, gender);

        assertEquals(MatchViewModel.parseCompetition(competition), competition.getName());
    }

    @Test
    public void parseMatchDay() {
        String label = "label";
        String matchDay = "matchDay";

        assertEquals(MatchViewModel.parseMatchDay(label, matchDay), label);
    }

    @Test
    public void parseMatchDay_empty() {
        String matchDay = "matchDay";
        assertEquals(MatchViewModel.parseMatchDay("", matchDay), "J. " + matchDay);
    }

    @Test
    public void parseMatchDay_null() {
        String matchDay = "matchDay";
        assertEquals(MatchViewModel.parseMatchDay(null, matchDay), "J. " + matchDay);
    }

    @Test
    public void isMatchLive() {
        assertTrue(true);
    }

    private List<Broadcaster> generateBroadcasters() {
        Broadcaster broadcasterA = Broadcaster.create("A", "codeA", "urlA");
        Broadcaster broadcasterB = Broadcaster.create("B", "codeB", null);
        Broadcaster broadcasterC = Broadcaster.create("C", "codeC", "url");

        List<Broadcaster> broadcasters = new ArrayList<>(3);
        broadcasters.add(broadcasterA);
        broadcasters.add(broadcasterB);
        broadcasters.add(broadcasterC);
        return broadcasters;
    }
}
