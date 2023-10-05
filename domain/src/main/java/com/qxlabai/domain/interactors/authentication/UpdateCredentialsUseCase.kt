package com.qxlabai.domain.interactors.authentication

import com.qxlabai.domain.events.Events
import com.qxlabai.domain.interactors.BaseUseCase
import com.qxlabai.domain.interactors.xmpp.entity.Credentials
import com.qxlabai.domain.repositories.CredentialsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateCredentialsUseCase @Inject constructor(
    private val repository: CredentialsRepository
) : BaseUseCase<Credentials, Flow<Events<String>>> {


    override suspend fun invoke(params: Credentials): Flow<Events<String>> {
        return withContext(Dispatchers.IO) {
            return@withContext repository.updateCredentials(params)
        }
    }
}