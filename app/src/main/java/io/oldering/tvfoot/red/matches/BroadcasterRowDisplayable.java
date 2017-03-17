package io.oldering.tvfoot.red.matches;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class BroadcasterRowDisplayable {
  public abstract String getCode();

  public static Builder builder() {
    return new AutoValue_BroadcasterRowDisplayable.Builder();
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder setCode(String code);

    public abstract BroadcasterRowDisplayable build();
  }
}
