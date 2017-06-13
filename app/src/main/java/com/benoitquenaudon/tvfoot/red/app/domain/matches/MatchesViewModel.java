package com.benoitquenaudon.tvfoot.red.app.domain.matches;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.state.MatchesViewState;
import javax.inject.Inject;

import static com.benoitquenaudon.tvfoot.red.app.common.PreConditions.checkNotNull;

public class MatchesViewModel {
  private final MatchesAdapter adapter;

  public ObservableBoolean refreshLoading = new ObservableBoolean(false);
  public ObservableBoolean hasError = new ObservableBoolean(false);
  public ObservableBoolean hasData = new ObservableBoolean(false);
  public ObservableBoolean hasMore = new ObservableBoolean(true);
  public ObservableField<String> errorMessage = new ObservableField<>();
  private int currentPage = 0;

  @Inject MatchesViewModel(MatchesAdapter adapter) {
    this.adapter = adapter;
  }

  @SuppressWarnings("ThrowableResultOfMethodCallIgnored") //
  void updateFromState(MatchesViewState state) {
    updateCurrentPage(state);

    refreshLoading.set(state.refreshLoading());
    hasError.set(state.error() != null);
    hasData.set(!state.matches().isEmpty());
    hasMore.set(state.hasMore());

    if (hasError.get()) {
      Throwable error = checkNotNull(state.error(), "state error is null");
      setErrorMessage(error.toString());
    }
    if (hasData.get()) {
      adapter.setMatchesItems(state.matchesItemDisplayables(hasMore.get()));
    }
  }

  private void updateCurrentPage(MatchesViewState state) {
    currentPage = state.currentPage();
  }

  private void setErrorMessage(String message) {
    errorMessage.set(message);
  }

  public int getCurrentPage() {
    return currentPage;
  }
}
