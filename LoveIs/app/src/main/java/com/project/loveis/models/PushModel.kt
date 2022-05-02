package com.project.loveis.models

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class PushModel(
    val from: Long?,
    @Json(name = "meeting_id")
    val meetingId: Long?,
    val type: String
): Parcelable
