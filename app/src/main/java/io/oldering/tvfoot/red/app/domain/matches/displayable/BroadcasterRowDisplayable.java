package io.oldering.tvfoot.red.app.domain.matches.displayable;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class BroadcasterRowDisplayable {
  public abstract String code();

  public static Builder builder() {
    return new AutoValue_BroadcasterRowDisplayable.Builder();
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder code(String code);

    public abstract BroadcasterRowDisplayable build();
  }
}
