package com.project.loveis.models

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class MeetingType(
    val id: Long,
    val icon: String,
    val name: String,
    val locked: Boolean

): Parcelable
