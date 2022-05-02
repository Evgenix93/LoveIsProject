package com.project.loveis.singletones

import com.project.loveis.apis.*
import com.project.loveis.util.TokenInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object Network {
    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addNetworkInterceptor(TokenInterceptor())
        .callTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://loveis.scratch.studio/api/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val authApi: AuthApi
    get() = retrofit.create()

    val profileApi: ProfileApi
    get() = retrofit.create()

    val searchApi: SearchApi
    get() = retrofit.create()

    val loveIsEventIsApi: LoveIsEventIsApi
    get() = retrofit.create()

    val placeApi: PlaceApi
    get() = retrofit.create()

    val sabsriptionApi: SubscriptionApi
    get() = retrofit.create()

    val chatApi: ChatApi
    get() = retrofit.create()

    val walletApi: WalletApi
    get() = retrofit.create()

    val techSupportApi: TechSupportApi
    get() = retrofit.create()

    val videoApi: VideoApi
    get() = retrofit.create()
}