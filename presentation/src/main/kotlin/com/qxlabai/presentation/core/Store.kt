package com.qxlabai.presentation.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
@OptIn(ExperimentalCoroutinesApi::class)
class Store<S : State, A : Action>(
    initialState: S,
    private val reducer: Reducer<S, A>,
    private val middleware: Middleware<S, A>
) {


    private val _state = MutableStateFlow(initialState)
    val state : StateFlow<S> = _state

    private val currentState: S
        get() = _state.value

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun dispatch (action: A) {
        middleware.process(action, currentState, this)
        _state.value = reducer.reduce(currentState, action)
    }
}
