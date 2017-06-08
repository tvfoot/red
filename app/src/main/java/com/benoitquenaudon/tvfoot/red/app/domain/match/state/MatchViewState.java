package com.benoitquenaudon.tvfoot.red.app.domain.match.state;

import com.benoitquenaudon.tvfoot.red.app.domain.match.MatchDisplayable;
import com.google.auto.value.AutoValue;
import javax.annotation.Nullable;

@AutoValue public abstract class MatchViewState {
  @Nullable public abstract MatchDisplayable match();

  @Nullable public abstract Throwable error();

  public abstract boolean loading();

  public abstract boolean shouldNotifyMatchStart();

  public static Builder builder() {
    return new AutoValue_MatchViewState.Builder();
  }

  public abstract Builder buildWith();

  static MatchViewState idle() {
    return MatchViewState.builder().shouldNotifyMatchStart(true).loading(false).build();
  }

  @AutoValue.Builder public static abstract class Builder {
    public abstract Builder match(MatchDisplayable match);

    public abstract Builder error(@Nullable Throwable error);

    public abstract Builder loading(boolean loading);

    public abstract Builder shouldNotifyMatchStart(boolean should);

    public abstract MatchViewState build();
  }
}
