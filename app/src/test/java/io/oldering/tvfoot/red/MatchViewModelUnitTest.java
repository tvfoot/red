package io.oldering.tvfoot.red;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

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

import static io.oldering.tvfoot.red.util.TimeConstants.ONE_MINUTE_IN_MILLIS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MatchViewModel.class)
public class MatchViewModelUnitTest {

    // TODO(benoit) not sure how to test create here
    // 1. all parse* and isMatchLive are called
    // plus, they are passed to the AutoValue constructor which is also called
    // @Test
    // public void create() {
    //
    //     Date date = mock(Date.class);
    //     Date date2 = mock(Date.class);
    //
    //     MatchViewModel matchVM = PowerMockito.mock(MatchViewModel.class);
    //     matchVM.getCompetition();
    //
    //     when(matchVM.parseStartTime(date)).thenReturn("startTime");
    //
    ////     mockStatic(MatchViewModel.class);
    ////     when(MatchViewModel.parseStartTime(date)).thenReturn("startTime");
    //
    //     Team homeTeam = mock(Team.class);
    //     when(homeTeam.isEmpty()).thenReturn(true);
    //
    //     Competition competition = mock(Competition.class);
    //     when(competition.getName()).thenReturn("competition name");
    //
    //     Match match = mock(Match.class);
    //     when(match.getStartAt()).thenReturn(date);
    //     when(match.getHomeTeam()).thenReturn(homeTeam);
    //     when(match.getCompetition()).thenReturn(competition);
    //     when(match.getLabel()).thenReturn("match label");
    //     matchVM.create(match);
    //
    //     verify(matchVM).parseStartTime(date2);
    //     verify(matchVM).parseHeadLine(homeTeam, null, "yoyo");
    // }
    //
    // @Test
    // public void create_otherversion() {
    //     // TODO(benoit) need to test
    //     // 1. all parse* and isMatchLive are called
    //     // plus, they are passed to the AutoValue constructor which is also called
    //
    //     Date date = mock(Date.class);
    //     Date date2 = mock(Date.class);
    //     List<Broadcaster> broadcasters = new ArrayList<>();
    //     List<BroadcasterViewModel> broadcasterVMs = new ArrayList<>();
    //
    //     Team homeTeam = mock(Team.class);
    //     when(homeTeam.isEmpty()).thenReturn(true);
    //
    //     String matchLabel = "matchLabel";
    //
    //     String competitionName = "competitionName";
    //     Competition competition = mock(Competition.class);
    //     when(competition.getName()).thenReturn(competitionName);
    //
    //     mockStatic(MatchViewModel.class);
    //     when(MatchViewModel.parseStartTime(date)).thenReturn("startTime");
    //     when(MatchViewModel.parseBroadcasters(broadcasters)).thenReturn(broadcasterVMs);
    //     when(MatchViewModel.parseHeadLine(homeTeam, null, matchLabel)).thenReturn(matchLabel);
    //     when(MatchViewModel.parseCompetition(competition)).thenReturn(competitionName);
    //     when(MatchViewModel.parseMatchDay(matchLabel, null)).thenReturn(matchLabel);
    //     when(MatchViewModel.isMatchLive(date)).thenReturn(true);
    //
    //     Match match = mock(Match.class);
    //     when(match.getStartAt()).thenReturn(date);
    //     when(match.getHomeTeam()).thenReturn(homeTeam);
    //     when(match.getCompetition()).thenReturn(competition);
    //     when(match.getLabel()).thenReturn(matchLabel);
    //     MatchViewModel.create(match);
    //
    //     verifyStatic();
    //     verify(MatchViewModel.parseStartTime(date2));
    // }

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
    public void isMatchLive_notYet() {
        Date startDate = new Date(System.currentTimeMillis() + ONE_MINUTE_IN_MILLIS);

        assertFalse(MatchViewModel.isMatchLive(startDate));
    }

    @Test
    public void isMatchLive_AlmostFinished() {
        Date startDate = new Date(System.currentTimeMillis() - ONE_MINUTE_IN_MILLIS);

        assertTrue(MatchViewModel.isMatchLive(startDate));
    }

    @Test
    public void isMatchLive_JustStarted() {
        Date startDate = new Date(System.currentTimeMillis() - 104 * ONE_MINUTE_IN_MILLIS);

        assertTrue(MatchViewModel.isMatchLive(startDate));
    }

    @Test
    public void isMatchLive_notAnymore() {
        Date startDate = new Date(System.currentTimeMillis() - 106 * ONE_MINUTE_IN_MILLIS);

        assertFalse(MatchViewModel.isMatchLive(startDate));
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
