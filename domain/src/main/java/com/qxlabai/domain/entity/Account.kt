package com.qxlabai.domain.entity

import com.qxlabai.domain.entity.AccountStatus.*
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
private val String.localPartDomainPart: Pair<String, String>
    get() {
        val (localPart, domainPart) = this.split("@")
        return Pair(localPart, domainPart)
    }