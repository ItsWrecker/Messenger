package com.qxlabai.data.datastore.entiry

import com.qxlabai.domain.entity.AccountStatus
import kotlinx.serialization.Serializable

@Serializable
data class Preferences(
    val connectionAvailability: Boolean,
    val connectionAuthenticated: Boolean,
    val accountStatus: AccountStatus,
    val accountJid: String,
    val accountLocalPart: String,
    val accountDomainPart: String,
    val accountPassword: String,
) {
   companion object {
       fun getDefaultInstances() = Preferences(
           connectionAvailability = false,
           connectionAuthenticated = false,
           accountStatus = AccountStatus.NotSet,
           accountLocalPart = "",
           accountDomainPart = "",
           accountPassword = "",
           accountJid = ""
       )
   }
}