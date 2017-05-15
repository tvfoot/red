package com.benoitquenaudon.tvfoot.red.app.data.entity;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import javax.annotation.Nullable;

@AutoValue public abstract class Competition {
  public static TypeAdapter<Competition> typeAdapter(Gson gson) {
    return new AutoValue_Competition.GsonTypeAdapter(gson);
  }

  public abstract String code();

  public abstract String name();

  @Nullable public abstract String country();

  @Nullable public abstract String url();

  @Nullable public abstract String gender();
}
