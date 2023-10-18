package com.qxlabai.data.datastore.repository

import com.qxlabai.data.databse.dao.SendingChatStateDao
import com.qxlabai.data.databse.entity.SendingChatStateEntity
import com.qxlabai.data.databse.entity.asExternalModel
import com.qxlabai.data.datastore.entiry.asEntity
import com.qxlabai.domain.collectors.SendingChatStatesRepository
import com.qxlabai.domain.entity.SendingChatState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SendingChatStatesRepositoryImpl @Inject constructor(
    private val sendingChatStateDao: SendingChatStateDao
) : SendingChatStatesRepository{

    override fun getSendingChatStatesStream(): Flow<List<SendingChatState>> =
        sendingChatStateDao.getSendingChatStateEntitiesStream()
            .map { it.map(SendingChatStateEntity::asExternalModel) }

    override suspend fun updateSendingChatState(sendingChatState: SendingChatState) =
        sendingChatStateDao.upsert(sendingChatState.asEntity())
}