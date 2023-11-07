package com.qxlabai.messenger.core.common.utils

import android.text.TextUtils

val String.isValidJid: Boolean
    get() = !TextUtils.isEmpty(this) && this.length > 3

val String.isValidPasscode: Boolean
    get() = TextUtils.isEmpty(this).not() && android.util.Patterns.PHONE.matcher(this)
        .matches() && this.length == 4