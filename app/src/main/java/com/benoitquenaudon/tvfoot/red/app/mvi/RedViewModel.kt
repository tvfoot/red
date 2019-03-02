package com.benoitquenaudon.tvfoot.red.app.mvi

import androidx.lifecycle.ViewModel

abstract class RedViewModel<I : MviIntent, S : MviViewState> : MviViewModel<I, S>, ViewModel()