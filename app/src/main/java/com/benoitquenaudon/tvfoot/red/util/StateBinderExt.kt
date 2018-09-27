package com.benoitquenaudon.tvfoot.red.util

import com.benoitquenaudon.tvfoot.red.app.mvi.MviAction
import com.benoitquenaudon.tvfoot.red.app.mvi.MviIntent
import com.benoitquenaudon.tvfoot.red.app.mvi.MviResult
import com.benoitquenaudon.tvfoot.red.app.mvi.MviViewState
import com.benoitquenaudon.tvfoot.red.app.mvi.RedViewModel
import timber.log.Timber

fun RedViewModel<out MviIntent, out MviViewState>.logIntent(intent: MviIntent) {
  Timber.d("Intent: %s", intent)
}

fun RedViewModel<out MviIntent, out MviViewState>.logAction(action: MviAction) {
  Timber.d("Action: %s", action)
}

fun RedViewModel<out MviIntent, out MviViewState>.logResult(result: MviResult) {
  Timber.d("Result: %s", result)
}

fun RedViewModel<out MviIntent, out MviViewState>.logState(state: MviViewState) {
  Timber.d("State: %s", state)
}