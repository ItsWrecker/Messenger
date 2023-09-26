package com.qxlabai.presentation.core

interface Middleware <S: State, A: Action> {
    suspend fun process(
        action: A,
        currentState: S,
        store: Store<S, A>
    )
}