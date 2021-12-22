package com.project.loveis.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoveIs(
    val id: Int,
    val date: String,
    @Json(name = "telegram_url")
    val telegramUrl: String?,
    @Json(name = "whatsapp_url")
    val whatsAppUrl: String?,
    val status: String,
    @Json(name = "inviting_user_complete")
    val invitingUserComplete:Boolean,
    @Json(name = "invited_user_complete")
    val invitedUserComplete: Boolean,
    @Json(name = "inviting_user")
    val invitingUser:Int,
    @Json(name = "invited_user")
    val invitedUser:Int,
    val place: Int,
    val type:Int
)
