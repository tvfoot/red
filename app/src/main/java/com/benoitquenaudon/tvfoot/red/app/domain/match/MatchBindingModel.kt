package com.benoitquenaudon.tvfoot.red.app.domain.match

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import javax.inject.Inject

class MatchBindingModel @Inject constructor(
    private val broadcastersAdapter: MatchBroadcastersAdapter
) {
  val isLoading = ObservableBoolean()
  val hasError = ObservableBoolean(false)
  val hasData = ObservableBoolean(false)
  val shouldNotifyMatchStart = ObservableBoolean(true)
  val match = ObservableField<MatchDisplayable>()
  val errorMessage = ObservableField<String>()

  fun updateFromState(state: MatchViewState) {
    isLoading.set(state.loading)
    hasError.set(state.error != null)
    hasData.set(state.match != null)
    shouldNotifyMatchStart.set(state.shouldNotifyMatchStart)

    if (hasError.get()) {
      val error = checkNotNull(state.error) { "state error is null" }
      errorMessage.set(error.toString())
    }
    if (hasData.get() && //
        (match.get() == null || match.get() != state.match)) {
      match.set(state.match)
      broadcastersAdapter.addAll(match.get().broadcasters())
    }
  }
}
