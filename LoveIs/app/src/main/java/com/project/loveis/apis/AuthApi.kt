package com.project.loveis.apis

import com.project.loveis.models.Phone
import com.project.loveis.models.PhoneWithCode
import com.project.loveis.models.TokenData
import com.project.loveis.models.TokenResponse
import okhttp3.*
import retrofit2.Response
import retrofit2.http.*

interface AuthApi {

    @GET("phone/")
    suspend fun verifyPhone(@Query("phone") phone: String, @Query("debug") debug: Boolean): retrofit2.Response<Unit>

    @POST("phone/")
    suspend fun submitPhone(@Body phoneWithCode: PhoneWithCode): retrofit2.Response<Unit>

    @POST("token/")
    suspend fun getToken(@Body tokenData: TokenData): retrofit2.Response<TokenResponse>

    @POST("user/signup/")
    @Multipart
    suspend fun registerNewUser(@PartMap info: Map<String, @JvmSuppressWildcards RequestBody>, @Part photo: MultipartBody.Part?): retrofit2.Response<Unit>

    @PATCH("user/")
    suspend fun changePhone(@Body newPhone: Phone): Response<Unit>


}