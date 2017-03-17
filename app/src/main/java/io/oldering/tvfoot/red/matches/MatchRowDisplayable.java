package io.oldering.tvfoot.red.matches;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import io.oldering.tvfoot.red.data.entity.Broadcaster;
import io.oldering.tvfoot.red.data.entity.Competition;
import io.oldering.tvfoot.red.data.entity.Match;
import io.oldering.tvfoot.red.data.entity.Team;
import io.oldering.tvfoot.red.util.StringUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static io.oldering.tvfoot.red.util.TimeConstants.ONE_MATCH_TIME_IN_MILLIS;

@AutoValue public abstract class MatchRowDisplayable {
  private static SimpleDateFormat shortDateFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
  private static SimpleDateFormat simpleDateFormat =
      new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
  private static SimpleDateFormat fullTextDateFormat =
      new SimpleDateFormat("EEEE dd MMMM yyyy à HH'h'mm", Locale.FRANCE);

  public abstract String getHeaderKey();

  public abstract String getStartTime();

  public abstract List<BroadcasterRowDisplayable> getBroadcasters();

  public abstract String getHeadline();

  public abstract String getCompetition();

  public abstract String getMatchDay();

  public abstract boolean isLive();

  public abstract String getStartTimeInText();

  public abstract String getHomeTeamDrawableName();

  public abstract String getAwayTeamDrawableName();

  public abstract String getLocation();

  public abstract String getMatchId();

  public static MatchRowDisplayable fromMatch(Match match) {
    return new AutoValue_MatchRowDisplayable( //
        parseHeaderKey(match.startAt()), //
        parseStartTime(match.startAt()), //
        parseBroadcasters(match.broadcasters()), //
        parseHeadLine(match.homeTeam(), match.awayTeam(), match.label()), //
        parseCompetition(match.competition()), //
        parseMatchDay(match.label(), match.matchDay()), //
        isMatchLive(match.startAt()), //
        parseStartTimeInText(match.startAt()), //
        parseHomeTeamDrawableName(match.homeTeam()), //
        parseAwayTeamDrawableName(match.awayTeam()), //
        parseLocation(match), match.id());
  }

  public static List<MatchRowDisplayable> fromMatches(List<Match> matches) {
    List<MatchRowDisplayable> matchRowDisplayables = new ArrayList<>(matches.size());
    for (Match match : matches) {
      matchRowDisplayables.add(fromMatch(match));
    }
    return matchRowDisplayables;
  }

  private static String parseHeaderKey(Date startAt) {
    simpleDateFormat.setTimeZone(TimeZone.getDefault());
    return simpleDateFormat.format(startAt);
  }

  public static String parseStartTime(Date startAt) {
    shortDateFormat.setTimeZone(TimeZone.getDefault());
    return shortDateFormat.format(startAt);
  }

  public static List<BroadcasterRowDisplayable> parseBroadcasters(
      @Nullable List<Broadcaster> broadcasters) {
    if (broadcasters == null) {
      return new ArrayList<>();
    }

    List<BroadcasterRowDisplayable> broadcastersVM = new ArrayList<>(broadcasters.size());
    for (Broadcaster broadcaster : broadcasters) {
      broadcastersVM.add(BroadcasterRowDisplayable.builder().setCode(broadcaster.code()).build());
    }
    return broadcastersVM;
  }

  public static String parseHeadLine(Team homeTeam, Team awayTeam, @Nullable String matchLabel) {
    if (homeTeam.isEmpty() || awayTeam.isEmpty()) {
      return String.valueOf(matchLabel);
    }
    return String.valueOf(homeTeam.name()).toUpperCase() + " - " + String.valueOf(awayTeam.name())
        .toUpperCase();
  }

  public static String parseCompetition(Competition competition) {
    return competition.name();
  }

  public static String parseMatchDay(@Nullable String matchLabel, @Nullable String matchDay) {
    if (matchLabel != null && !matchLabel.trim().isEmpty()) {
      return matchLabel;
    } else {
      return "J. " + String.valueOf(matchDay);
    }
  }

  public static boolean isMatchLive(Date startAt) {
    long now = Calendar.getInstance().getTimeInMillis();
    long startTimeInMillis = startAt.getTime();
    return now >= startTimeInMillis && now <= startTimeInMillis + ONE_MATCH_TIME_IN_MILLIS;
  }

  private static String parseStartTimeInText(Date startAt) {
    fullTextDateFormat.setTimeZone(TimeZone.getDefault());
    return StringUtils.capitalize(fullTextDateFormat.format(startAt));
  }

  private static String parseHomeTeamDrawableName(Team homeTeam) {
    String code = homeTeam.code();
    if (code != null) {
      return code;
    }
    return Team.DEFAULT_CODE;
  }

  private static String parseAwayTeamDrawableName(Team awayTeam) {
    String code = awayTeam.code();
    if (code != null) {
      return code;
    }
    return Team.DEFAULT_CODE;
  }

  private static String parseLocation(Match match) {
    return String.valueOf(match.place());
  }
}
