package com.project.loveis.models

import com.project.loveis.util.OperationType
import com.squareup.moshi.JsonClass

@JsonClass (generateAdapter = true)
data class Operation(
    val id: Long,
    val value: Int,
    val type: OperationType,
    val timestamp: String,
    val extra: Map<String,User>?
)
