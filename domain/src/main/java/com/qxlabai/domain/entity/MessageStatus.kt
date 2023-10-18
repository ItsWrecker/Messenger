package com.qxlabai.domain.entity

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
