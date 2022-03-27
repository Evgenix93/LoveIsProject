package com.project.loveis.apis

import com.project.loveis.models.OperationsWrapper
import com.project.loveis.models.Wallet
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface WalletApi {

    @POST("user/wallet/")
   suspend fun addMoney(@Body value: Wallet): Response<Any>

   @POST("withdrawal/")
   suspend fun getMoney(@Body value: Wallet): Response<Any>

   @POST("user/wallet/history/")
   suspend fun getOperations(@Query("page") page: Int, @Query("size") size: Int): Response<OperationsWrapper>
}