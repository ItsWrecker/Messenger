package com.qxlabai.domain.interactors

interface BaseUseCase<in P, out R> {
    suspend operator fun invoke(params: P): R
}