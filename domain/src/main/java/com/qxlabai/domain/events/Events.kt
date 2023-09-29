package com.qxlabai.domain.events

sealed interface Events<out R> {
    data class Loading(val message: String) : Events<Nothing>
    data class Error(val message: String, val cause: Exception?= null) : Events<Nothing>
    data class Success<R>(val data: R) : Events<R>
}