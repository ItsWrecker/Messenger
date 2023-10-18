package com.qxlabai.domain.entity

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
    RegistrationAlreadyExist
}
