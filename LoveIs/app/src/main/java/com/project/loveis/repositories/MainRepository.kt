package com.project.loveis.repositories


import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.net.toFile
import com.project.loveis.apis.ProfileApi
import com.project.loveis.models.*
import com.project.loveis.singletones.Network
import com.project.loveis.singletones.ProfileId
import com.project.loveis.singletones.ProfileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.net.URI

class MainRepository(val context: Context) {
    private val profileApi = Network.profileApi
    private val photo = File(context.cacheDir, "photo.jpg")

    suspend fun getCurrentUserInfo(): Response<User>?{
        return try{
            profileApi.getUserInfoById(ProfileId.id)
        }catch (e: Throwable){
            Log.d("debug", e.message.toString())
            if(e is IOException) null
            else{
                try {
                    val response = profileApi.getUserInfoByIdGetError(ProfileId.id)
                    Response.error(-1, response.body()?.detail?.toResponseBody(null)!!)
                }catch (e: Throwable){
                    val response = profileApi.getUserInfoByIdGetErrors(ProfileId.id)
                    Response.error(-1, response.body()?.errors?.keys?.joinToString(",")?.toResponseBody(null)!!)
                }
            }
        }
    }

    suspend fun getUserById(id: Long): Response<User>?{
        return try{
            profileApi.getUserInfoById(id)
        }catch (e: Throwable){
            Log.d("debug", e.message.toString())
            if(e is IOException) null
            else{
                try {
                    val response = profileApi.getUserInfoByIdGetError(id)
                    Response.error(-1, response.body()?.detail?.toResponseBody(null)!!)
                }catch (e: Throwable){
                    val response = profileApi.getUserInfoByIdGetErrors(id)
                    Response.error(-1, response.body()?.errors?.keys?.joinToString(",")?.toResponseBody(null)!!)
                }
            }
        }
    }

    suspend fun updateUserPhoto(photo: Uri): Response<User>?{
        return withContext(Dispatchers.IO) {
            val file = File(context.cacheDir, "image.jpg")
            Log.d("debug", photo.path.toString())
            context.contentResolver.openInputStream(photo).use { input ->
                file.outputStream().use { output ->
                    input?.copyTo(output)
                }

            }
             try {
                val part = MultipartBody.Part.createFormData(
                    "photo", file.name,
                    file.asRequestBody("image/*".toMediaTypeOrNull())
                )
                val response = profileApi.updateUserPhoto(part)
                response
            } catch (e: Throwable) {
                Log.d("debug", e.message.toString())
                null
            }
        }
    }

    suspend fun updateAdditionalPhoto(photo: Uri): Response<User>?{
        return withContext(Dispatchers.IO) {
            val file = File(context.cacheDir, "image2.jpg")
            Log.d("debug", photo.path.toString())
            context.contentResolver.openInputStream(photo).use { input ->
                file.outputStream().use { output ->
                    input?.copyTo(output)
                }

            }
            try {
                val part = MultipartBody.Part.createFormData(
                    "url", file.name,
                    file.asRequestBody("image/*".toMediaTypeOrNull())
                )

                val response = profileApi.updateAdditionalPhoto(part)
                response
            } catch (e: Throwable) {
                Log.d("debug", e.message.toString())
                null
            }
        }
    }

    suspend fun deleteAdditionalPhoto(uuid: String): Response<User>? {
        return try{
            profileApi.deleteAdditionalPhoto(uuid)
        }catch (e: Throwable){
            null
        }
    }

    suspend fun updateUserInfo(name: String, about: String): Response<User>?{
        val part1 = MultipartBody.Part.createFormData("name", null, name.toRequestBody())
        val part2 = MultipartBody.Part.createFormData("about", null, about.toRequestBody())
        return try {
            profileApi.updateUserInfo(part1, part2)
        }catch (e: Throwable){
            null
        }
    }

    suspend fun verify(photo: Uri): Response<Unit>? {
        val file = File(context.cacheDir, "image.jpg")
        context.contentResolver.openInputStream(photo).use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }

        }
        val photoPart =
            MultipartBody.Part.createFormData("photo", file.name, file.asRequestBody())
        return try {
             Network.profileApi.verify(photoPart)
        } catch (e: Exception) {
            Log.e("MyTag", "error = ${e.message}")
            null
        }
    }

    suspend fun uploadPhoto(uri: Uri): File {
        return withContext(Dispatchers.IO) {
            photo.outputStream().use { outputStream ->
                context.contentResolver.openInputStream(uri).use { inputStream ->
                        inputStream?.copyTo(outputStream)
                }
            }
            photo
        }
    }

    suspend fun searchUsers(ageFrom: Int, ageTo: Int, gender: String): Response<SearchResult>? {
      return  try {
            Network.searchApi.searchUsers(ageFrom, ageTo, gender)
        } catch (e: Exception){
            null
        }
    }

    suspend fun updateCoordinates(coordinates: Coordinates): Response<Coordinates>?{
        return  try {
            profileApi.updateCoordinates(coordinates)
        } catch (e: Exception){
            Log.d("debug", e.message.toString())
            if(e is IOException) null
            else{
                try {
                    val response = profileApi.updateCoordinatesGetError(coordinates)
                    Response.error(-1, response.body()?.detail?.toResponseBody(null)!!)
                }catch (e: Throwable){
                    val response = profileApi.updateCoordinatesGetErrors(coordinates)
                    Response.error(-1, response.body()?.errors?.keys?.joinToString(",")?.toResponseBody(null)!!)
                }
            }
        }
    }

    fun setUpCurrentUser(user: User){
        ProfileInfo.currentUser = user
    }

    fun getCurrentUser(): User?{
        return ProfileInfo.currentUser
    }

    fun getShareUserIntent(userId: Long): Intent{
        val uri = Uri.withAppendedPath(Uri.parse("https://loveis.scratch.studio/profile/"), userId.toString())
        return Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, uri.toString())
            type = "text/plain"


        }
    }

}