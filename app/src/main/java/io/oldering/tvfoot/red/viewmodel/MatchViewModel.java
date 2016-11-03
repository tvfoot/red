package io.oldering.tvfoot.red.viewmodel;

import android.os.Parcelable;
import android.view.View;

import com.google.auto.value.AutoValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.oldering.tvfoot.red.model.Broadcaster;
import io.oldering.tvfoot.red.model.Competition;
import io.oldering.tvfoot.red.model.Match;
import io.oldering.tvfoot.red.model.Team;
import io.oldering.tvfoot.red.util.rxbus.RxBus;
import io.oldering.tvfoot.red.util.rxbus.event.MatchClickEvent;

/**
 * TODO(benoit) think about splitting this into two view model, listrow and detail
 * TODO(benoit) should not probably use AutoValue here... troublesome about DI and stuff.
 */
@AutoValue
public abstract class MatchViewModel implements Parcelable {

    private static SimpleDateFormat shortDateFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
    private static SimpleDateFormat FullTextDateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy à HH'h'mm", Locale.FRANCE);
    private static long ONE_MATCH_TIME_IN_MILLIS = 105 * 60 * 1000;

    private RxBus rxBus;

    public abstract String getStartTime();

    public abstract List<BroadcasterViewModel> getBroadcasters();

    public abstract String getHeadline();

    public abstract String getCompetition();

    public abstract String getMatchDay();

    public abstract boolean isLive();

    public abstract String getStartTimeInText();

    public abstract String getHomeTeamDrawableName();

    public abstract String getAwayTeamDrawableName();

    public abstract String getLocation();

    public abstract String getMatchId();

    public static MatchViewModel create(Match match, RxBus rxBus) {
        MatchViewModel matchViewModel = new AutoValue_MatchViewModel(
                parseStartTime(match.getStartAt()),
                parseBroadcasters(match.getBroadcasters()),
                parseHeadLine(match.getHomeTeam(), match.getAwayTeam(), match.getLabel()),
                parseCompetition(match.getCompetition()),
                parseMatchDay(match.getLabel(), match.getMatchday()),
                isMatchLive(match.getStartAt()),
                parseStartTimeInText(match.getStartAt()),
                parseHomeTeamDrawableName(match.getHomeTeam()),
                parseAwayTeamDrawableName(match.getAwayTeam()),
                parseLocation(match),
                match.getId());
        matchViewModel.setRxBus(rxBus);
        return matchViewModel;
    }

    public static String parseStartTime(Date startAt) {
        shortDateFormat.setTimeZone(TimeZone.getDefault());
        return shortDateFormat.format(startAt);
    }

    public static List<BroadcasterViewModel> parseBroadcasters(List<Broadcaster> broadcasters) {
        if (broadcasters == null) {
            return new ArrayList<>();
        }

        List<BroadcasterViewModel> broadcastersVM = new ArrayList<>(broadcasters.size());
        for (Broadcaster broadcaster : broadcasters) {
            broadcastersVM.add(BroadcasterViewModel.create(broadcaster));
        }
        return broadcastersVM;
    }

    public static String parseHeadLine(Team homeTeam, Team awayTeam, String matchLabel) {
        if (homeTeam.isEmpty() || awayTeam.isEmpty()) {
            return matchLabel;
        }
        return String.valueOf(homeTeam.getName()).toUpperCase() +
                " - " +
                String.valueOf(awayTeam.getName()).toUpperCase();
    }

    public static String parseCompetition(Competition competition) {
        return competition.getName();
    }

    public static String parseMatchDay(String matchLabel, String matchDay) {
        if (matchLabel != null && !matchLabel.trim().isEmpty()) {
            return matchLabel;
        } else {
            return "J. " + matchDay;
        }
    }

    public static boolean isMatchLive(Date startAt) {
        long now = Calendar.getInstance().getTimeInMillis();
        long startTimeInMillis = startAt.getTime();
        return now >= startTimeInMillis && now <= startTimeInMillis + ONE_MATCH_TIME_IN_MILLIS;
    }

    private static String parseStartTimeInText(Date startAt) {
        FullTextDateFormat.setTimeZone(TimeZone.getDefault());
        return FullTextDateFormat.format(startAt);
    }

    private static String parseHomeTeamDrawableName(Team homeTeam) {
        // TODO(benoit) check null
        if (homeTeam.getCode() != null) {
            return homeTeam.getCode().toLowerCase();
        }
        return Team.DEFAULT_CODE;
    }

    private static String parseAwayTeamDrawableName(Team awayTeam) {
        // TODO(benoit) check null
        if (awayTeam.getCode() != null) {
            return awayTeam.getCode().toLowerCase();
        }
        return Team.DEFAULT_CODE;
    }

    private static String parseLocation(Match match) {
        return match.getPlace();
    }

    public void onMatchClick(View ignored) {
        if (rxBus.hasObservers()) {
            rxBus.send(new MatchClickEvent(this));
        }
    }

    public void setRxBus(RxBus rxBus) {
        this.rxBus = rxBus;
    }
}
