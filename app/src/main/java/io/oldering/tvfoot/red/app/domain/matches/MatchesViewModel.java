package io.oldering.tvfoot.red.app.domain.matches;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import io.oldering.tvfoot.red.app.domain.matches.state.MatchesViewState;
import javax.inject.Inject;

import static io.oldering.tvfoot.red.app.common.PreConditions.checkNotNull;

public class MatchesViewModel {
  private final MatchesAdapter adapter;

  public ObservableBoolean refreshLoading = new ObservableBoolean(false);
  public ObservableBoolean hasError = new ObservableBoolean(false);
  public ObservableBoolean hasData = new ObservableBoolean(false);
  public ObservableField<String> errorMessage = new ObservableField<>();
  private int currentPage = 0;

  @Inject MatchesViewModel(MatchesAdapter adapter) {
    this.adapter = adapter;
  }

  @SuppressWarnings("ThrowableResultOfMethodCallIgnored") void updateFromState(
      MatchesViewState state) {
    updateCurrentPage(state);

    refreshLoading.set(state.refreshLoading());
    hasError.set(state.error() != null);
    hasData.set(!state.matches().isEmpty());

    if (hasError.get()) {
      Throwable error = checkNotNull(state.error(), "state error is null");
      setErrorMessage(error.toString());
    }
    if (hasData.get()) {
      adapter.setMatchesItems(state.matchesItemDisplayables());
    }
  }

  private void updateCurrentPage(MatchesViewState state) {
    //if (state.currentPage() > 0) {
    currentPage = state.currentPage();
    //} else {
    //  Timber.d("CONNARD currentPage is 0 and %s", state);
    //}
  }

  private void setErrorMessage(String message) {
    errorMessage.set(message);
  }

  public int getCurrentPage() {
    return currentPage;
  }
}
