package com.qxlabai.messenger.core.data.repository

import com.qxlabai.messenger.core.model.data.SendingChatState
import kotlinx.coroutines.flow.Flow

interface SendingChatStatesRepository {

    fun getSendingChatStatesStream(): Flow<List<SendingChatState>>

    suspend fun updateSendingChatState(sendingChatState: SendingChatState)
}
