package com.qxlabai.presentation.core

interface Reducer <S: State, A: Action> {

    suspend fun reduce(currentState: S, action: A): S
}