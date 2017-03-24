package io.oldering.tvfoot.red.match;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class MatchViewState {

  public static Builder builder() {
    return new AutoValue_MatchViewState.Builder();
  }

  public abstract Builder buildWith();

  @AutoValue.Builder public static abstract class Builder {
    public abstract MatchViewState build();
  }

  public enum Status {
    MATCH_LOADING, MATCH_ERROR, MATCH_LOADED, //
  }
}
