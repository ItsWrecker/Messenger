package com.qxlabai.messenger.core.data.repository

import com.qxlabai.messenger.core.model.data.SendingChatState
import com.qxlabai.messenger.core.data.model.asEntity
import com.qxlabai.messenger.core.database.dao.SendingChatStateDao
import com.qxlabai.messenger.core.database.model.SendingChatStateEntity
import com.qxlabai.messenger.core.database.model.asExternalModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SendingChatStatesRepositoryImpl @Inject constructor(
    private val sendingChatStateDao: SendingChatStateDao
) : SendingChatStatesRepository {

    override fun getSendingChatStatesStream(): Flow<List<SendingChatState>> =
        sendingChatStateDao.getSendingChatStateEntitiesStream()
            .map { it.map(SendingChatStateEntity::asExternalModel) }

    override suspend fun updateSendingChatState(sendingChatState: SendingChatState) =
        sendingChatStateDao.upsert(sendingChatState.asEntity())
}
