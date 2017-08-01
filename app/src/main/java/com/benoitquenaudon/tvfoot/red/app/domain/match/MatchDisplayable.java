package com.benoitquenaudon.tvfoot.red.app.domain.match;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import com.benoitquenaudon.tvfoot.red.app.data.entity.Broadcaster;
import com.benoitquenaudon.tvfoot.red.app.data.entity.Competition;
import com.benoitquenaudon.tvfoot.red.app.data.entity.Match;
import com.benoitquenaudon.tvfoot.red.app.data.entity.Team;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.BroadcasterRowDisplayable;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.displayable.MatchesItemDisplayable;
import com.google.auto.value.AutoValue;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import kotlin.text.StringsKt;

import static com.benoitquenaudon.tvfoot.red.app.common.PreConditions.checkNotNull;

@AutoValue public abstract class MatchDisplayable implements Parcelable, MatchesItemDisplayable {
  private static final ThreadLocal<DateFormat> shortDateFormat = new ThreadLocal<DateFormat>() {
    @Override protected DateFormat initialValue() {
      SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());
      format.setTimeZone(TimeZone.getDefault());
      return format;
    }
  };
  private static final ThreadLocal<DateFormat> mediumDateFormat = new ThreadLocal<DateFormat>() {
    @Override protected DateFormat initialValue() {
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
      format.setTimeZone(TimeZone.getDefault());
      return format;
    }
  };
  private static final ThreadLocal<DateFormat> fullTextDateFormat = new ThreadLocal<DateFormat>() {
    @Override protected DateFormat initialValue() {
      SimpleDateFormat format =
          new SimpleDateFormat("EEEE dd MMMM yyyy HH'h'mm", Locale.getDefault());
      format.setTimeZone(TimeZone.getDefault());
      return format;
    }
  };

  public abstract String headerKey();

  public abstract long startAt();

  public abstract String startTime();

  public abstract List<BroadcasterRowDisplayable> broadcasters();

  public abstract String headline();

  public abstract String competition();

  public abstract String matchDay();

  public abstract boolean live();

  public abstract String startTimeInText();

  public abstract String homeTeamLogoPath();

  public abstract String awayTeamLogoPath();

  @Nullable public abstract String location();

  public abstract String matchId();

  public static MatchDisplayable fromMatch(Match match) {
    return new AutoValue_MatchDisplayable( //
        parseHeaderKey(match.getStartAt()), //
        match.getStartAt().getTime(), //
        parseStartTime(match.getStartAt()), //
        parseBroadcasters(match.getBroadcasters()), //
        parseHeadLine(match.getHomeTeam(), match.getAwayTeam(), match.getLabel()), //
        parseCompetition(match.getCompetition()), //
        parseMatchDay(match.getLabel(), match.getMatchDay()), //
        isMatchLive(match.getStartAt()), //
        parseStartTimeInText(match.getStartAt()), //
        parseTeamLogoPath(match.getHomeTeam()), //
        parseTeamLogoPath(match.getAwayTeam()), //
        parseLocation(match), match.getId());
  }

  private static String parseHeaderKey(Date startAt) {
    return mediumDateFormat.get().format(startAt);
  }

  private static String parseStartTime(Date startAt) {
    return shortDateFormat.get().format(startAt);
  }

  private static List<BroadcasterRowDisplayable> parseBroadcasters(
      @Nullable List<Broadcaster> broadcasters) {
    if (broadcasters == null) {
      return new ArrayList<>();
    }

    List<BroadcasterRowDisplayable> broadcastersVM = new ArrayList<>(broadcasters.size());
    for (Broadcaster broadcaster : broadcasters) {
      broadcastersVM.add(
          BroadcasterRowDisplayable.create(broadcaster.getName(), broadcaster.getCode()));
    }
    return broadcastersVM;
  }

  private static String parseHeadLine(Team homeTeam, Team awayTeam, @Nullable String matchLabel) {
    if (homeTeam.isEmpty() || awayTeam.isEmpty()) {
      return String.valueOf(matchLabel);
    }
    return String.valueOf(homeTeam.getName()).toUpperCase() + " - " + String.valueOf(
        awayTeam.getName()).toUpperCase();
  }

  private static String parseCompetition(Competition competition) {
    return competition.getName();
  }

  private static String parseMatchDay(@Nullable String matchLabel, @Nullable String matchDay) {
    if (matchLabel != null && !matchLabel.isEmpty()) {
      return matchLabel;
    } else {
      return "J. " + matchDay;
    }
  }

  private static boolean isMatchLive(Date startAt) {
    long now = Calendar.getInstance().getTimeInMillis();
    long startTimeInMillis = startAt.getTime();
    return now >= startTimeInMillis && now <= startTimeInMillis + TimeUnit.MINUTES.toMillis(105);
  }

  private static String parseStartTimeInText(Date startAt) {
    return StringsKt.capitalize(fullTextDateFormat.get().format(startAt));
  }

  private static String parseTeamLogoPath(Team team) {
    String path;
    if (team.getCode() == null || team.getType() == null) {
      path = "/images/teams/default/large/default.png";
    } else {
      String type = checkNotNull(team.getType(), "team's type should not be null");
      String code = checkNotNull(team.getCode(), "team's code should not be null");
      switch (type) {
        case "nation":
          path = String.format("/images/teams/nations/large/%s.png", code.toLowerCase());
          break;
        case "club":
          String country = checkNotNull(team.getCountry(), "team's country should not be null");
          path = String.format("/images/teams/%s/large/%s.png", country, code.toLowerCase());
          break;
        default:
          throw new IllegalStateException("Unkown type " + type);
      }
    }
    return path;
  }

  @Nullable private static String parseLocation(Match match) {
    return match.getPlace();
  }

  @Override public boolean isSameAs(MatchesItemDisplayable newItem) {
    return newItem instanceof MatchDisplayable && this.matchId()
        .equals(((MatchDisplayable) newItem).matchId());
  }
}
