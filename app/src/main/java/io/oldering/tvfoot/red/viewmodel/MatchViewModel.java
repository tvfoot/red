package io.oldering.tvfoot.red.viewmodel;

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

@AutoValue
public abstract class MatchViewModel {
    private static final String TAG = "MatchViewModel";
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
    private static long ONE_MATCH_TIME = 105 * 60;

    public abstract String getStartTime();

    public abstract List<BroadcasterViewModel> getBroadcasters();

    public abstract String getHeadline();

    public abstract String getCompetition();

    public abstract String getMatchDay();

    public abstract boolean isLive();

    static MatchViewModel create(Match match) {
        return new AutoValue_MatchViewModel(
                parseStartTime(match.getStartAt()),
                parseBroadcasters(match.getBroadcasters()),
                parseHeadLine(match.getHomeTeam(), match.getAwayTeam(), match.getLabel()),
                parseCompetition(match.getCompetition()),
                parseMatchDay(match.getLabel(), match.getMatchday()),
                isMatchLive(match.getStartAt()));
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
        return now >= startTimeInMillis && now <= startTimeInMillis + ONE_MATCH_TIME;
    }
}
