package com.qxlabai.data.datastore.entiry

data class Credentials(
    val uuid: String,
    val passcode: String,
    val lastLogin: Long = 0
)