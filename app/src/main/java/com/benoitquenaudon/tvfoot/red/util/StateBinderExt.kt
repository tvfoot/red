package com.benoitquenaudon.tvfoot.red.util

import android.os.Bundle
import com.benoitquenaudon.tvfoot.red.app.mvi.MviAction
import com.benoitquenaudon.tvfoot.red.app.mvi.MviIntent
import com.benoitquenaudon.tvfoot.red.app.mvi.MviResult
import com.benoitquenaudon.tvfoot.red.app.mvi.MviViewState
import com.benoitquenaudon.tvfoot.red.app.mvi.RedViewModel
import timber.log.Timber

fun RedViewModel<out MviIntent, out MviViewState>.logIntent(intent: MviIntent) {
  Timber.d("Intent: %s", intent)
  val params = Bundle()
  params.putString("intent", intent.toString())
  firebaseAnalytics.logEvent("intent", params)
}

fun RedViewModel<out MviIntent, out MviViewState>.logAction(action: MviAction) {
  Timber.d("Action: %s", action)
  val params = Bundle()
  params.putString("action", action.toString())
  firebaseAnalytics.logEvent("action", params)
}

fun RedViewModel<out MviIntent, out MviViewState>.logResult(result: MviResult) {
  Timber.d("Result: %s", result)
  val params = Bundle()
  params.putString("result", result.toString())
  firebaseAnalytics.logEvent("result", params)
}

fun RedViewModel<out MviIntent, out MviViewState>.logState(state: MviViewState) {
  Timber.d("State: %s", state)
  val params = Bundle()
  params.putString("state", state.toString())
  firebaseAnalytics.logEvent("state", params)
}