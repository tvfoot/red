package io.oldering.tvfoot.red.app.data.entity;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;

@AutoValue public abstract class Match {
  public static TypeAdapter<Match> typeAdapter(Gson gson) {
    return new AutoValue_Match.GsonTypeAdapter(gson);
  }

  public abstract String id();

  @Nullable public abstract String label();

  @SerializedName("start-at") public abstract Date startAt();

  @SerializedName("matchday") @Nullable public abstract String matchDay();

  @SerializedName("home-team") public abstract Team homeTeam();

  @SerializedName("away-team") public abstract Team awayTeam();

  @Nullable public abstract List<Broadcaster> broadcasters();

  @Nullable public abstract String place();

  public abstract Competition competition();

  public abstract boolean postponed();
}
