package com.project.loveis.apis

import com.project.loveis.models.SubsriptionRequestData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SubscriptionApi {

    @POST("user/subscription/")
    suspend fun confirmSubsription(@Body data: SubsriptionRequestData): Response<Unit>
}