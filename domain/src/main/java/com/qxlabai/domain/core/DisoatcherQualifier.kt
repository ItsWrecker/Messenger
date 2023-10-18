package com.qxlabai.domain.core

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dialogueDispatcher: MessengerDispatchers)