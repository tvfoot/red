package com.benoitquenaudon.tvfoot.red.util

import com.benoitquenaudon.tvfoot.red.app.mvi.MviAction
import com.benoitquenaudon.tvfoot.red.app.mvi.MviIntent
import com.benoitquenaudon.tvfoot.red.app.mvi.MviResult
import com.benoitquenaudon.tvfoot.red.app.mvi.MviViewState
import com.benoitquenaudon.tvfoot.red.app.mvi.RedViewModel
import timber.log.Timber

fun RedViewModel<out MviIntent, out MviViewState>.logIntent(intent: MviIntent) {
  Timber.d("Intent: %s", intent)
  firebaseAnalytics.logEvent("intent", "intent" to intent.toString())
}

fun RedViewModel<out MviIntent, out MviViewState>.logAction(action: MviAction) {
  Timber.d("Action: %s", action)
  firebaseAnalytics.logEvent("action", "action" to action.toString())
}

fun RedViewModel<out MviIntent, out MviViewState>.logResult(result: MviResult) {
  Timber.d("Result: %s", result)
  firebaseAnalytics.logEvent("result", "result" to result.toString())
}

fun RedViewModel<out MviIntent, out MviViewState>.logState(state: MviViewState) {
  Timber.d("State: %s", state)
  firebaseAnalytics.logEvent("state", "state" to state.toString())
}