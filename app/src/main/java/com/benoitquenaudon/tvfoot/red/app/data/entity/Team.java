package com.benoitquenaudon.tvfoot.red.app.data.entity;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import javax.annotation.Nullable;

@AutoValue public abstract class Team {
  public static final String DEFAULT_CODE = "default";

  public static TypeAdapter<Team> typeAdapter(Gson gson) {
    return new AutoValue_Team.GsonTypeAdapter(gson);
  }

  @Nullable public abstract String code();

  @Nullable public abstract String name();

  @Nullable public abstract String fullname();

  @Nullable public abstract String city();

  @Nullable public abstract String country();

  @Nullable public abstract String url();

  @Nullable public abstract String stadium();

  @Nullable public abstract String twitter();

  @Nullable public abstract String type();

  public boolean isEmpty() {
    return name() == null;
  }
}
