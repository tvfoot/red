package io.oldering.tvfoot.red.matches;

import com.google.auto.value.AutoValue;

@AutoValue public abstract class LoadingRowDisplayable implements MatchesItemDisplayable {
  @Override public boolean isSameAs(MatchesItemDisplayable newItem) {
    return newItem instanceof LoadingRowDisplayable;
  }

  public static LoadingRowDisplayable create() {
    return new AutoValue_LoadingRowDisplayable();
  }
}
