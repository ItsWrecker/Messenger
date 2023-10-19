package com.qxlabai.messenger.core.common.coroutines

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val messengerDispatchers: MessengerDispatchers)

enum class MessengerDispatchers {
    IO
}
