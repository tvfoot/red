package com.benoitquenaudon.tvfoot.red.util

import android.os.Bundle
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchAction
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchIntent
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchResult
import com.benoitquenaudon.tvfoot.red.app.domain.match.state.MatchViewState
import com.benoitquenaudon.tvfoot.red.app.mvi.StateBinder
import timber.log.Timber

fun StateBinder.logIntent(intent: MatchIntent) {
  Timber.d("Intent: %s", intent)
  val params = Bundle()
  params.putString("intent", intent.toString())
  firebaseAnalytics.logEvent("intent", params)
}

fun StateBinder.logAction(action: MatchAction) {
  Timber.d("Action: %s", action)
  val params = Bundle()
  params.putString("action", action.toString())
  firebaseAnalytics.logEvent("action", params)
}

fun StateBinder.logResult(result: MatchResult) {
  Timber.d("Result: %s", result)
  val params = Bundle()
  params.putString("result", result.toString())
  firebaseAnalytics.logEvent("result", params)
}

fun StateBinder.logState(state: MatchViewState) {
  Timber.d("State: %s", state)
  val params = Bundle()
  params.putString("state", state.toString())
  firebaseAnalytics.logEvent("state", params)
}