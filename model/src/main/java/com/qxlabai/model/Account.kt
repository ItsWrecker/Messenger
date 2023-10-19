package com.qxlabai.model

import com.qxlabai.model.AccountStatus.Disabled
import com.qxlabai.model.AccountStatus.Offline
import com.qxlabai.model.AccountStatus.Online
import com.qxlabai.model.AccountStatus.ShouldLogin

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
