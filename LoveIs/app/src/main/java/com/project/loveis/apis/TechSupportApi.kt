package com.project.loveis.apis

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface TechSupportApi {

    @Multipart
    @POST("support/")
   suspend fun sendRequest(@Part content: MultipartBody.Part): Response<Unit>
}