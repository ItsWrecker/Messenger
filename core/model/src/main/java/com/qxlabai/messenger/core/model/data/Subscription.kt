package com.qxlabai.messenger.core.model.data


data class Subscription(
    val subscriptionFrom: String,
    val isApproved: Boolean = false,
    val isDeclined: Boolean = false,
    val isPending: Boolean = true
) {
    companion object {
        fun createApprove(jid: String) = listOf(
            Subscription(
                subscriptionFrom = jid,
                isApproved = true,
                isDeclined = false,
                isPending = false
            )
        )

        fun createDecline(jid: String) = listOf(
            Subscription(
                subscriptionFrom = jid,
                isApproved = false,
                isDeclined = true,
                isPending = false
            )
        )
    }
}