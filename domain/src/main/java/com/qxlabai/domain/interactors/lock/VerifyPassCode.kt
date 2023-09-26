package com.qxlabai.domain.interactors.lock

import com.qxlabai.domain.events.Events
import com.qxlabai.domain.interactors.BaseUseCase
import com.qxlabai.domain.repositories.AppLockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class VerifyPassCode @Inject constructor(
    private val repository: AppLockRepository
) : BaseUseCase<String, Flow<Events<Boolean>>> {
    override suspend fun invoke(params: String): Flow<Events<Boolean>> {
        return repository.verifyPassCode(params)
    }
}