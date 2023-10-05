package com.qxlabai.domain.interactors.authentication

import com.qxlabai.domain.events.Events
import com.qxlabai.domain.interactors.BaseUseCase
import com.qxlabai.domain.interactors.xmpp.entity.Credentials
import com.qxlabai.domain.repositories.CredentialsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FetchSavedCredentialsUseCase @Inject constructor(
    private val repository: CredentialsRepository
) : BaseUseCase<Unit, Flow<Events<Credentials>>> {

    override suspend fun invoke(params: Unit): Flow<Events<Credentials>> {
        return withContext(Dispatchers.IO) {
            return@withContext repository.fetchCredentials()
        }
    }
}