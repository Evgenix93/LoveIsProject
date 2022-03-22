package com.project.loveis.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class LoveIs(
    val id: Long,
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
    val invitingUser:User,
    @Json(name = "invited_user")
    val invitedUser:User,
    val place: Place,
    val type: MeetingType
): Parcelable
