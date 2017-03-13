package io.oldering.tvfoot.red.data.model;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;

@AutoValue public abstract class Match {
  public static TypeAdapter<Match> typeAdapter(Gson gson) {
    return new AutoValue_Match.GsonTypeAdapter(gson);
  }

  public static Match create(String id, @Nullable String label, Date startAt,
      @Nullable String matchDay, Team homeTeam, Team awayTeam,
      @Nullable List<Broadcaster> broadcasters, @Nullable String place, Competition competition,
      boolean isPostponed) {
    return new AutoValue_Match(id, label, startAt, matchDay, homeTeam, awayTeam, broadcasters,
        place, competition, isPostponed);
  }

  public abstract String getId();

  @Nullable public abstract String getLabel();

  @SerializedName("start-at") public abstract Date getStartAt();

  @Nullable public abstract String getMatchday();

  @SerializedName("home-team") public abstract Team getHomeTeam();

  @SerializedName("away-team") public abstract Team getAwayTeam();

  @Nullable public abstract List<Broadcaster> getBroadcasters();

  @Nullable public abstract String getPlace();

  public abstract Competition getCompetition();

  public abstract boolean isPostponed();
}
