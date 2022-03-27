package com.project.loveis.repositories

import android.util.Log
import com.project.loveis.singletones.Network
import okhttp3.MultipartBody
import retrofit2.Response

class TechSupportRepository {

   suspend fun sendRequest(text: String): Response<Unit>? {
      return try {
          Network.techSupportApi.sendRequest(MultipartBody.Part.createFormData("content", text))
      }catch (e: Throwable){
          Log.e("MyDebug", "request error = ${e.message}")
          null
      }
    }
}