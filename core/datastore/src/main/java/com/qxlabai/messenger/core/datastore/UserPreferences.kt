package com.qxlabai.messenger.core.datastore

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val connectionAvailability: Boolean = false,
    val connectionAuthenticated: Boolean = false,
    val accountJid: String = "",
    val accountLocalPart: String = "",
    val accountDomainPart: String = "",
    val accountPassword: String = "",
    val accountStatus: AccountStatus = AccountStatus.NotSet,
    val themeBranding: ThemeBranding = ThemeBranding.Default,
    val darkConfig: DarkConfig = DarkConfig.Dark
) {

    @Serializable
    enum class AccountStatus {
        NotSet,
        ShouldRegister,
        Registering,
        ShouldLogin,
        LoggingIn,
        Disabled,
        Offline,
        Online,
        Unauthorized,
        ServerNotFound,
        RegistrationSuccessful,
        RegistrationFailed,
        RegistrationAlreadyExist,
    }


    @Serializable
    enum class ThemeBranding {
        Default,
        Android
    }

    @Serializable
    enum class DarkConfig {
        System,
        Light,
        Dark
    }
}
