package com.qxlabai.messenger.core.model.data

enum class MessageStatus {
    ShouldSend,
    Sending,
    Sent,
    Received,
    SentFailed,
    SentDelivered,
    SentDisplayed,
    ReceivedDisplayed
}
