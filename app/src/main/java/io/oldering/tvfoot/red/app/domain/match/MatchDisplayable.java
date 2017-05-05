package io.oldering.tvfoot.red.app.domain.match;

import com.google.auto.value.AutoValue;
import io.oldering.tvfoot.red.app.data.entity.Broadcaster;
import io.oldering.tvfoot.red.app.data.entity.Competition;
import io.oldering.tvfoot.red.app.data.entity.Match;
import io.oldering.tvfoot.red.app.data.entity.Team;
import io.oldering.tvfoot.red.app.domain.matches.displayable.BroadcasterRowDisplayable;
import io.oldering.tvfoot.red.app.domain.matches.displayable.MatchesItemDisplayable;
import io.oldering.tvfoot.red.util.StringUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.annotation.Nullable;

import static io.oldering.tvfoot.red.app.common.PreConditions.checkNotNull;
import static io.oldering.tvfoot.red.app.common.TimeConstants.ONE_MATCH_TIME_IN_MILLIS;

@AutoValue public abstract class MatchDisplayable implements MatchesItemDisplayable {
  private static SimpleDateFormat shortDateFormat =
      new SimpleDateFormat("HH:mm", Locale.getDefault());
  private static SimpleDateFormat mediumDateFormat =
      new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
  private static SimpleDateFormat fullTextDateFormat =
      new SimpleDateFormat("EEEE dd MMMM yyyy HH'h'mm", Locale.getDefault());

  public abstract String headerKey();

  public abstract String startTime();

  public abstract List<BroadcasterRowDisplayable> broadcasters();

  public abstract String headline();

  public abstract String competition();

  public abstract String matchDay();

  public abstract boolean live();

  public abstract String startTimeInText();

  public abstract String homeTeamLogoPath();

  public abstract String awayTeamLogoPath();

  public abstract String location();

  public abstract String matchId();

  public static MatchDisplayable fromMatch(Match match) {
    return new AutoValue_MatchDisplayable( //
        parseHeaderKey(match.startAt()), //
        parseStartTime(match.startAt()), //
        parseBroadcasters(match.broadcasters()), //
        parseHeadLine(match.homeTeam(), match.awayTeam(), match.label()), //
        parseCompetition(match.competition()), //
        parseMatchDay(match.label(), match.matchDay()), //
        isMatchLive(match.startAt()), //
        parseStartTimeInText(match.startAt()), //
        parseTeamLogoPath(match.homeTeam()), //
        parseTeamLogoPath(match.awayTeam()), //
        parseLocation(match), match.id());
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
      broadcastersVM.add(BroadcasterRowDisplayable.create(broadcaster.code()));
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

  private static String parseTeamLogoPath(Team team) {
    String path;
    if (team.code() == null || team.type() == null) {
      path = "/images/teams/default/large/default.png";
    } else {
      String type = checkNotNull(team.type(), "team's type should not be null");
      String code = checkNotNull(team.code(), "team's code should not be null");
      switch (type) {
        case "nation":
          path = String.format("/images/teams/nations/large/%s.png", code.toLowerCase());
          break;
        case "club":
          String country = checkNotNull(team.country(), "team's country should not be null");
          path = String.format("/images/teams/%s/large/%s.png", country, code.toLowerCase());
          break;
        default:
          throw new IllegalStateException("Unkown type " + type);
      }
    }
    return path;
  }

  private static String parseLocation(Match match) {
    return String.valueOf(match.place());
  }

  @Override public boolean isSameAs(MatchesItemDisplayable newItem) {
    return newItem instanceof MatchDisplayable && this.matchId()
        .equals(((MatchDisplayable) newItem).matchId());
  }
}
