package com.qxlabai.domain.interactors.lock

import com.qxlabai.domain.events.Events
import com.qxlabai.domain.interactors.BaseUseCase
import com.qxlabai.domain.repositories.AppLockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SetPassCodeUseCase @Inject constructor(
    private val repository: AppLockRepository
) : BaseUseCase<String, Unit> {

    override suspend fun invoke(params: String) {
        return repository.setPassCode(params)
    }
}