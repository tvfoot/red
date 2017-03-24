package io.oldering.tvfoot.red.match;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;

@AutoValue public abstract class MatchViewState {
  @Nullable public abstract MatchDisplayable match();

  public abstract Status status();

  @Nullable public abstract Throwable error();

  public static Builder builder() {
    return new AutoValue_MatchViewState.Builder();
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder match(MatchDisplayable match);

    public abstract Builder status(Status status);

    public abstract Builder error(Throwable error);

    public abstract MatchViewState build();
  }

  public enum Status {
    MATCH_LOADING, MATCH_ERROR, MATCH_LOADED, //
  }
}
