package io.oldering.tvfoot.red.app.data.entity;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import javax.annotation.Nullable;

@AutoValue public abstract class Broadcaster {
  public static TypeAdapter<Broadcaster> typeAdapter(Gson gson) {
    return new AutoValue_Broadcaster.GsonTypeAdapter(gson);
  }

  public abstract String name();

  public abstract String code();

  @Nullable public abstract String url();
}
