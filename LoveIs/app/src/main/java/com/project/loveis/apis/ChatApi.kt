package com.project.loveis.apis

import com.project.loveis.models.ChatMessage
import com.project.loveis.models.Dialog
import com.project.loveis.models.DialogsWrapper
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ChatApi {

    @Multipart
    @POST("chat/{userId}/")
   suspend fun sendMessage(@Path("userId") userId: Long, @Part message: MultipartBody.Part?, @Part attachment: MultipartBody.Part?,
                           @Part attachment2: MultipartBody.Part? = null,
                           @Part attachment3: MultipartBody.Part? = null,
                           @Part attachment4: MultipartBody.Part? = null,
                           @Part attachment5: MultipartBody.Part? = null,
                           @Part attachment6: MultipartBody.Part? = null,
                           @Part attachment7: MultipartBody.Part? = null,
                           @Part attachment8: MultipartBody.Part? = null,
                           @Part attachment9: MultipartBody.Part? = null,
                           @Part attachment10: MultipartBody.Part? = null): Response<Any>

   @GET("chat/{userId}/")
   suspend fun getMessages(@Path("userId") userId: Long): Response<Dialog>

   @GET("chat/")
   suspend fun getDialogs(): Response<DialogsWrapper>

}