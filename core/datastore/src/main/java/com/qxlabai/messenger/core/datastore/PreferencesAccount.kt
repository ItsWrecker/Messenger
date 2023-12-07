package com.qxlabai.messenger.core.datastore

import com.qxlabai.messenger.core.datastore.UserPreferences.AccountStatus
import com.qxlabai.messenger.core.datastore.UserPreferences.AccountStatus.Disabled
import com.qxlabai.messenger.core.model.data.Account
import com.qxlabai.messenger.core.model.data.AccountStatus.valueOf

data class PreferencesAccount(
    val jid: String = "",
    val localPart: String = "",
    val domainPart: String = "",
    val password: String = "",
    val status: AccountStatus = Disabled
)

fun PreferencesAccount.asExternalModel() = Account(
    jid = jid,
    localPart = localPart,
    domainPart = domainPart,
    password = password,
    status = valueOf(status.name)
)
