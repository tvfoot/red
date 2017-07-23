package com.benoitquenaudon.tvfoot.red.app.mvi

import io.reactivex.Observable

/**
 * Object that will subscribes to a view's intents, process it and emit a state back.
 *
 * @param <I> Top class of the [MviIntent] that the [MviViewModel] will be subscribing to.
 * @param <S> Top class of the [MviViewState] the [MviViewModel] will be emitting
 */
interface MviViewModel<I : MviIntent, S : MviViewState> {
  fun processIntents(intents: Observable<I>)
  fun states(): Observable<S>
}
