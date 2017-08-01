package com.benoitquenaudon.tvfoot.red.app.data.entity;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.Date;
import java.util.List;
import javax.annotation.Nullable;

@AutoValue public abstract class Match {
  public static final String MATCH_ID = "MATCH_ID";

  public static JsonAdapter<Match> jsonAdapter(Moshi moshi) {
    return new AutoValue_Match.MoshiJsonAdapter(moshi);
  }

  public abstract String id();

  @Nullable public abstract String label();

  @Json(name = "start-at") public abstract Date startAt();

  @Json(name = "matchday") @Nullable public abstract String matchDay();

  @Json(name = "home-team") public abstract Team homeTeam();

  @Json(name = "away-team") public abstract Team awayTeam();

  @Nullable public abstract List<Broadcaster> broadcasters();

  @Nullable public abstract String place();

  public abstract Competition competition();

  public abstract boolean postponed();
}
