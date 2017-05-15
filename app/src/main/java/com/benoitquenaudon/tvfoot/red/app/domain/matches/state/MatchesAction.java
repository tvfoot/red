package com.benoitquenaudon.tvfoot.red.app.domain.matches.state;

import com.google.auto.value.AutoValue;

interface MatchesAction {
  @AutoValue abstract class GetLastStateAction implements MatchesAction {
    public static GetLastStateAction create() {
      return new AutoValue_MatchesAction_GetLastStateAction();
    }
  }

  @AutoValue abstract class RefreshAction implements MatchesAction {
    public static RefreshAction create() {
      return new AutoValue_MatchesAction_RefreshAction();
    }
  }

  @AutoValue abstract class LoadNextPageAction implements MatchesAction {
    public abstract int pageIndex();

    public static LoadNextPageAction create(int pageIndex) {
      return new AutoValue_MatchesAction_LoadNextPageAction(pageIndex);
    }
  }
}