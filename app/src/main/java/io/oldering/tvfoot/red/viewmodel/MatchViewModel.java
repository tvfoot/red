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
import io.reactivex.subjects.PublishSubject;

/**
 * TODO(benoit) think about splitting this into two view model, listrow and detail
 * TODO(benoit) should not probably use AutoValue here... troublesome about DI and stuff.
 */
@AutoValue
public abstract class MatchViewModel implements Parcelable {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
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

    public abstract String getSummary();

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
                parseSummary(match),
                match.getId());
        matchViewModel.setRxBus(rxBus);
        return matchViewModel;
    }

    public static String parseStartTime(Date startAt) {
        simpleDateFormat.setTimeZone(TimeZone.getDefault());
        return simpleDateFormat.format(startAt);
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
        // TODO(benoit)
        return "parseStartTimeInText";
    }

    private static String parseHomeTeamDrawableName(Team homeTeam) {
        // TODO(benoit) check null
        return homeTeam.getCode();
    }

    private static String parseAwayTeamDrawableName(Team awayTeam) {
        // TODO(benoit) check null
        return awayTeam.getCode();
    }

    private static String parseSummary(Match match) {
        // TODO(benoit)
        return "summary";
    }

    private PublishSubject<MatchViewModel> matchClickSubject;

    public void onMatchClick(View view) {
        if (rxBus.hasObservers()) {
            rxBus.send(new MatchClickEvent(this));
        }
    }

    public void setRxBus(RxBus rxBus) {
        this.rxBus = rxBus;
    }
}
