package com.project.loveis.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GcmDevice(
    @Json(name = "registration_id")
    val fireBaseToken: String,
    @Json(name = "cloud_message_type")
    val cloudMessageType: String


)
