package com.qxlabai.domain.interactors.xmpp

import com.qxlabai.domain.events.Events
import com.qxlabai.domain.interactors.BaseUseCase
import com.qxlabai.domain.interactors.xmpp.entity.Credentials
import com.qxlabai.domain.repositories.XmppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticateUseCase @Inject constructor(
    private val xmppRepository: XmppRepository
) : BaseUseCase<Credentials, Flow<Events<String>>> {

    override suspend fun invoke(params: Credentials): Flow<Events<String>> {
        return withContext(Dispatchers.IO) {
            return@withContext xmppRepository.authenticate(params.username, params.passcode)
        }
    }
}