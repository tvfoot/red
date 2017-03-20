package io.oldering.tvfoot.red.matches.displayable;

import android.support.annotation.VisibleForTesting;
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
import javax.annotation.Nullable;

import static io.oldering.tvfoot.red.util.TimeConstants.ONE_MATCH_TIME_IN_MILLIS;

@AutoValue public abstract class MatchRowDisplayable implements MatchesItemDisplayable {
  private static SimpleDateFormat shortDateFormat = new SimpleDateFormat("HH:mm", Locale.FRANCE);
  static SimpleDateFormat mediumDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.FRANCE);
  private static SimpleDateFormat fullTextDateFormat =
      new SimpleDateFormat("EEEE dd MMMM yyyy à HH'h'mm", Locale.FRANCE);

  public abstract String headerKey();

  public abstract String startTime();

  public abstract List<BroadcasterRowDisplayable> broadcasters();

  public abstract String headline();

  public abstract String competition();

  public abstract String matchDay();

  public abstract boolean live();

  public abstract String startTimeInText();

  public abstract String homeTeamDrawableName();

  public abstract String awayTeamDrawableName();

  public abstract String location();

  public abstract String matchId();

  @VisibleForTesting public static MatchRowDisplayable fromMatch(Match match) {
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
    mediumDateFormat.setTimeZone(TimeZone.getDefault());
    return mediumDateFormat.format(startAt);
  }

  private static String parseStartTime(Date startAt) {
    shortDateFormat.setTimeZone(TimeZone.getDefault());
    return shortDateFormat.format(startAt);
  }

  private static List<BroadcasterRowDisplayable> parseBroadcasters(
      @Nullable List<Broadcaster> broadcasters) {
    if (broadcasters == null) {
      return new ArrayList<>();
    }

    List<BroadcasterRowDisplayable> broadcastersVM = new ArrayList<>(broadcasters.size());
    for (Broadcaster broadcaster : broadcasters) {
      broadcastersVM.add(BroadcasterRowDisplayable.builder().code(broadcaster.code()).build());
    }
    return broadcastersVM;
  }

  private static String parseHeadLine(Team homeTeam, Team awayTeam, @Nullable String matchLabel) {
    if (homeTeam.isEmpty() || awayTeam.isEmpty()) {
      return String.valueOf(matchLabel);
    }
    return String.valueOf(homeTeam.name()).toUpperCase() + " - " + String.valueOf(awayTeam.name())
        .toUpperCase();
  }

  private static String parseCompetition(Competition competition) {
    return competition.name();
  }

  private static String parseMatchDay(@Nullable String matchLabel, @Nullable String matchDay) {
    if (matchLabel != null && !matchLabel.trim().isEmpty()) {
      return matchLabel;
    } else {
      return "J. " + matchDay;
    }
  }

  private static boolean isMatchLive(Date startAt) {
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

  @Override public boolean isSameAs(MatchesItemDisplayable newItem) {
    return newItem instanceof MatchRowDisplayable && this.matchId()
        .equals(((MatchRowDisplayable) newItem).matchId());
  }
}
