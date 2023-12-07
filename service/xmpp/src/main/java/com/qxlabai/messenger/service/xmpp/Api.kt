package com.qxlabai.messenger.service.xmpp

import androidx.annotation.Keep
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Api {

    @Headers(
        "Content-Type: application/json",
        "Authorization: key=AAAAQTO0Re4:APA91bEmg44jbkiJDRlAKWpkN4eU7eNaI84fnWrBErFgU6uHmN8oMIWNJgmb2DaUShKMf9nFA9K3650-uO6MxYRrCH332f0X9dhDilqBr2Ul8DJlWNDlgD6cHfYEb5pO2KxAChRrQ7R5"
    )
    @POST("fcm/send")
    suspend fun sendFcmMessage(
        @Body fcmMessage: FcmMessage
    ): Response<Any>
}

@Keep
data class FcmMessage(
    val to: String,
    val notification: FcmNotification
)

@Keep
data class FcmNotification(
    val title: String,
    val body: String
)