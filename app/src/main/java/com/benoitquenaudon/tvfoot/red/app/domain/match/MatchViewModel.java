package com.benoitquenaudon.tvfoot.red.app.domain.match;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchViewState;
import com.benoitquenaudon.tvfoot.red.app.domain.matches.BroadcastersAdapter;
import javax.inject.Inject;

import static com.benoitquenaudon.tvfoot.red.app.common.PreConditions.checkNotNull;

public class MatchViewModel {
  private final BroadcastersAdapter broadcastersAdapter;
  public ObservableBoolean isLoading = new ObservableBoolean();
  public ObservableBoolean hasError = new ObservableBoolean(false);
  public ObservableBoolean hasData = new ObservableBoolean(false);
  public ObservableBoolean shouldNotifyMatchStart = new ObservableBoolean(true);
  public ObservableField<MatchDisplayable> match = new ObservableField<>();
  public ObservableField<String> errorMessage = new ObservableField<>();

  @Inject MatchViewModel(BroadcastersAdapter broadcastersAdapter) {
    this.broadcastersAdapter = broadcastersAdapter;
  }

  @SuppressWarnings("ThrowableResultOfMethodCallIgnored") void updateFromState(
      MatchViewState state) {
    isLoading.set(state.getLoading());
    hasError.set(state.getError() != null);
    hasData.set(state.getMatch() != null);
    shouldNotifyMatchStart.set(state.getShouldNotifyMatchStart());

    if (hasError.get()) {
      Throwable error = checkNotNull(state.getError(), "state error is null");
      errorMessage.set(error.toString());
    }
    if (hasData.get() && //
        (match.get() == null || !match.get().equals(state.getMatch()))) {
      match.set(state.getMatch());
      broadcastersAdapter.addAll(match.get().broadcasters());
    }
  }
}
