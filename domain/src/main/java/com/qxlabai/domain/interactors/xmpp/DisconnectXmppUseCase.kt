package com.qxlabai.domain.interactors.xmpp

import com.qxlabai.domain.events.Events
import com.qxlabai.domain.interactors.BaseUseCase
import com.qxlabai.domain.repositories.XmppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DisconnectXmppUseCase @Inject constructor(
    private val xmppRepository: XmppRepository
) : BaseUseCase<Unit, Boolean> {

    override suspend fun invoke(params: Unit): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext xmppRepository.disconnect()
        }
    }
}