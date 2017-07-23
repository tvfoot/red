package com.benoitquenaudon.tvfoot.red.app.mvi

import io.reactivex.Observable

/**
 * Object representing a UI that will
 * a) emit its intents to a view model,
 * b) subscribes to a view model for rendering its UI.
 *
 * @param <I> Top class of the [MviIntent] that the [MviView] will be emitting.
 * @param <S> Top class of the [MviViewState] the [MviView] will be subscribing to.
 */
interface MviView<I : MviIntent, S : MviViewState> {
  fun intents(): Observable<I>
  fun render(state: S)
}
