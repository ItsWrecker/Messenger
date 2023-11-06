package com.qxlabai.messenger.core.common.utils

import android.text.TextUtils

val String.isValidJid: Boolean
    get() = !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()

val String.isValidPasscode: Boolean
    get() = TextUtils.isEmpty(this).not() && android.util.Patterns.PHONE.matcher(this)
        .matches() && this.length == 4