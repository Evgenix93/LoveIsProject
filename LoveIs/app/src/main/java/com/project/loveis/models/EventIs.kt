package com.project.loveis.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class EventIs(
    val id: Long,
    val date: String,
    val owner: User,
    @Json(name = "participants_count")
    val participantsCount: String,
    @Json(name = "telegram_url")
    val telegramUrl: String?,
    @Json(name = "whatsapp_url")
    val whatsAppUrl: String?,
    val status: String,
    val place: Place,
    val type: MeetingType,
    @Json(name = "persons_number")
    val personsNumber: Int,
    val price: Int
): Parcelable
