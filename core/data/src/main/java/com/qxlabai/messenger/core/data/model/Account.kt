package com.qxlabai.messenger.core.data.model

import com.qxlabai.messenger.core.model.data.Account
import com.qxlabai.messenger.core.datastore.PreferencesAccount
import com.qxlabai.messenger.core.datastore.UserPreferences.AccountStatus


fun Account.asPreferences() = PreferencesAccount(
    jid = jid,
    localPart = localPart,
    domainPart = domainPart,
    password = password,
    status = AccountStatus.valueOf(status.name)
)
