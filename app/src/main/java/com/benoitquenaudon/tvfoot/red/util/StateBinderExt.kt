package com.benoitquenaudon.tvfoot.red.util

import android.os.Bundle
import com.benoitquenaudon.tvfoot.red.app.mvi.MviAction
import com.benoitquenaudon.tvfoot.red.app.mvi.MviIntent
import com.benoitquenaudon.tvfoot.red.app.mvi.MviResult
import com.benoitquenaudon.tvfoot.red.app.mvi.MviViewState
import com.benoitquenaudon.tvfoot.red.app.mvi.StateBinder
import timber.log.Timber

fun StateBinder.logIntent(intent: MviIntent) {
  Timber.d("Intent: %s", intent)
  val params = Bundle()
  params.putString("intent", intent.toString())
  firebaseAnalytics.logEvent("intent", params)
}

fun StateBinder.logAction(action: MviAction) {
  Timber.d("Action: %s", action)
  val params = Bundle()
  params.putString("action", action.toString())
  firebaseAnalytics.logEvent("action", params)
}

fun StateBinder.logResult(result: MviResult) {
  Timber.d("Result: %s", result)
  val params = Bundle()
  params.putString("result", result.toString())
  firebaseAnalytics.logEvent("result", params)
}

fun StateBinder.logState(state: MviViewState) {
  Timber.d("State: %s", state)
  val params = Bundle()
  params.putString("state", state.toString())
  firebaseAnalytics.logEvent("state", params)
}