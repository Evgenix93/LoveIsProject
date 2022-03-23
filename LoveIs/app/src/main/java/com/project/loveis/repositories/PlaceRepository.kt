package com.project.loveis.repositories

import android.content.Context
import android.net.Uri
import android.util.Log
import com.project.loveis.models.PlaceListResult
import com.project.loveis.singletones.Network
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.File

class PlaceRepository(val context: Context) {

    suspend fun createPlace(name: String, address: String, photo: Uri): Response<Unit>?{
        val file = File(context.cacheDir, "image.jpg")
        context.contentResolver.openInputStream(photo).use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }

        }
        val photoPart =
            MultipartBody.Part.createFormData("photo", file.name, file.asRequestBody())
        return try {
            Network.placeApi.createPlace(name, address, photoPart)
        }catch (e: Exception){
            Log.e("Debug", "error ${e.message}")
            null
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