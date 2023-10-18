package com.qxlabai.domain.collectors

import com.qxlabai.domain.entity.SendingChatState
import kotlinx.coroutines.flow.Flow

interface SendingChatStatesRepository {
    fun getSendingChatStatesStream(): Flow<List<SendingChatState>>

    suspend fun updateSendingChatState(sendingChatState: SendingChatState)
}