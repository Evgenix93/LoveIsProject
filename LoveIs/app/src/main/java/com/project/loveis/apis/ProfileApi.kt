package com.project.loveis.apis


import com.project.loveis.models.User
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ProfileApi {

    @GET("user/{user_id}/")
    suspend fun getUserInfoById(@Path("user_id") id: Int): Response<User>

    @POST("user/")
    @Multipart
    suspend fun updateUserPhoto(@Part photo: MultipartBody.Part): Response<User>

    @POST("user/")
    @Multipart
    suspend fun updateUserInfo(@Part name: MultipartBody.Part, @Part about: MultipartBody.Part ): Response<User>




    @Multipart
    @POST("user/verification/")
    suspend fun verify(@Part photoPart: MultipartBody.Part): Response<Unit>

}