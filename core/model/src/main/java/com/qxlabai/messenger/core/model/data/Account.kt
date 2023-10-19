package com.qxlabai.messenger.core.model.data

import com.qxlabai.messenger.core.model.data.AccountStatus.Disabled
import com.qxlabai.messenger.core.model.data.AccountStatus.Offline
import com.qxlabai.messenger.core.model.data.AccountStatus.Online
import com.qxlabai.messenger.core.model.data.AccountStatus.ShouldLogin

data class Account(
    val jid: String,
    val localPart: String,
    val domainPart: String,
    val password: String,
    val status: AccountStatus
) {
    val alreadyLoggedIn: Boolean
        get() = status == Online ||
            status == Disabled ||
            status == Offline

    companion object {
        fun create(jid: String, password: String): Account {
            val (localPart, domainPart) = jid.localPartDomainPart
            return Account(
                jid = jid,
                localPart = localPart,
                domainPart = domainPart,
                password = password,
                status = ShouldLogin
            )
        }
    }
}

// TODO: consider jid comprises only local part and domain part for now
private val String.localPartDomainPart: Pair<String, String>
    get() {
        val (localPart, domainPart) = this.split("@")
        return Pair(localPart, domainPart)
    }
