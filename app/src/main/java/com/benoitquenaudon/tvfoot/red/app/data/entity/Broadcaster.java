package com.benoitquenaudon.tvfoot.red.app.data.entity;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import javax.annotation.Nullable;

@AutoValue public abstract class Broadcaster {
  public static JsonAdapter<Broadcaster> jsonAdapter(Moshi moshi) {
    return new AutoValue_Broadcaster.MoshiJsonAdapter(moshi);
  }

  public abstract String name();

  public abstract String code();

  @Nullable public abstract String url();
}
