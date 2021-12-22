package com.project.loveis.util

import com.project.loveis.singletones.Tokens
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader("Authorization", Tokens.token)
            .build()

        return chain.proceed(newRequest)

    }
}