package io.oldering.tvfoot.red.matches;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import io.oldering.tvfoot.red.matches.state.MatchesViewState;
import javax.inject.Inject;

public class MatchesViewModel {
  private final MatchesAdapter adapter;

  public ObservableBoolean isFirstLoading = new ObservableBoolean(false);
  public ObservableBoolean hasError = new ObservableBoolean(false);
  public ObservableBoolean hasData = new ObservableBoolean(false);
  public ObservableField<String> errorMessage = new ObservableField<>();
  private int currentPage = 0;

  @Inject MatchesViewModel(MatchesAdapter adapter) {
    this.adapter = adapter;
  }

  void updateFromState(MatchesViewState state) {
    updateCurrentPage(state);

    switch (state.status()) {
      case FIRST_PAGE_IN_FLIGHT:
        isFirstLoading.set(true);
        hasError.set(false);
        hasData.set(false);
        break;
      case FIRST_PAGE_FAILURE:
        isFirstLoading.set(false);
        hasError.set(true);
        hasData.set(false);

        Throwable throwable = state.throwable();
        assert throwable != null;
        setErrorMessage(throwable);
        break;
      case FIRST_PAGE_SUCCESS:
        isFirstLoading.set(false);
        hasError.set(false);
        hasData.set(true);
        adapter.setMatchesItems(state.matchesItemDisplayables());
        break;
      case NEXT_PAGE_IN_FLIGHT:
        isFirstLoading.set(false);
        hasError.set(false);
        hasData.set(true);
        break;
      case NEXT_PAGE_FAILURE:
        isFirstLoading.set(false);
        hasError.set(true);
        hasData.set(false);

        throwable = state.throwable();
        assert throwable != null;
        setErrorMessage(throwable);
        break;
      case NEXT_PAGE_SUCCESS:
        isFirstLoading.set(false);
        hasError.set(false);
        hasData.set(true);
        adapter.setMatchesItems(state.matchesItemDisplayables());
        break;
      case MATCH_ROW_CLICK:
      case IDLE:
        break;
      default:
        throw new IllegalStateException("Don't know how to deal with this State " + state);
    }
  }

  private void updateCurrentPage(MatchesViewState state) {
    if (state.currentPage() > 0) {
      currentPage = state.currentPage();
    }
  }

  private void setErrorMessage(Throwable throwable) {
    errorMessage.set(throwable.toString());
  }

  public int currentPage() {
    return currentPage;
  }
}
