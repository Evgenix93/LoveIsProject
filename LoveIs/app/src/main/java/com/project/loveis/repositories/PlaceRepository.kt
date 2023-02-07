package com.project.loveis.repositories

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.ContentResolverCompat
import com.project.loveis.models.PlaceListResult
import com.project.loveis.singletones.Network
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.File

class PlaceRepository(val context: Context) {

    suspend fun createPlace(name: String, address: String, photo: Uri?): Response<Unit>?{
        return try {
        if(photo == null)
            Network.placeApi.createPlace(name, address, null)
        else {

            val file = File(context.cacheDir, "photo.${context.contentResolver.getType(photo)?.substringAfter('/') ?: "jpeg"}")
            context.contentResolver.openInputStream(photo).use { input ->
                file.outputStream().use { output ->
                    input?.copyTo(output)
                }

            }
            val photoPart =
                MultipartBody.Part.createFormData("photo", file.name, file.asRequestBody())
            Network.placeApi.createPlace(name, address, photoPart)
        }
        }catch (e: Exception){
            Log.e("Debug", "error ${e.message}")
            Response.error(400, e.message.orEmpty().toResponseBody())
        }

    }

    suspend fun getPlaces(): Response<PlaceListResult>?{
        return try {
            Network.placeApi.getPlaces()
        }catch (e: Exception){
            null
        }
    }



}