package com.benoitquenaudon.tvfoot.red.app.data.entity;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import javax.annotation.Nullable;

@AutoValue public abstract class Competition {
  public static JsonAdapter<Competition> jsonAdapter(Moshi moshi) {
    return new AutoValue_Competition.MoshiJsonAdapter(moshi);
  }

  public abstract String code();

  public abstract String name();

  @Nullable public abstract String country();

  @Nullable public abstract String url();

  @Nullable public abstract String gender();
}
