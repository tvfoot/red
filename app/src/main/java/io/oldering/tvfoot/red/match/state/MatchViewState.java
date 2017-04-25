package io.oldering.tvfoot.red.match.state;

import android.support.annotation.Nullable;
import com.google.auto.value.AutoValue;
import io.oldering.tvfoot.red.match.MatchDisplayable;

import static io.oldering.tvfoot.red.match.state.MatchViewState.Status.IDLE;

@AutoValue public abstract class MatchViewState {
  @Nullable public abstract MatchDisplayable match();

  public abstract Status status();

  @Nullable public abstract Throwable error();

  public abstract boolean loading();

  public static Builder builder() {
    return new AutoValue_MatchViewState.Builder();
  }

  public abstract Builder buildWith();

  static MatchViewState idle() {
    return MatchViewState.builder().status(IDLE).loading(false).build();
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder match(MatchDisplayable match);

    public abstract Builder status(Status status);

    public abstract Builder error(Throwable error);

    public abstract Builder loading(boolean loading);

    public abstract MatchViewState build();
  }

  public enum Status {
    LOAD_MATCH_IN_FLIGHT, LOAD_MATCH_FAILURE, LOAD_MATCH_SUCCESS, //
    IDLE
  }
}
