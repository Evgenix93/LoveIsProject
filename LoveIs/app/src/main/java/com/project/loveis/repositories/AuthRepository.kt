package com.project.loveis.repositories

import android.content.Context
import android.net.Uri
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.net.toUri
import com.project.loveis.models.*
import com.project.loveis.singletones.Network
import com.project.loveis.singletones.ProfileId
import com.project.loveis.singletones.ProfileInfo
import com.project.loveis.singletones.Tokens
import com.project.loveis.util.CloudMessageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.util.*

class AuthRepository(val context: Context) {
    private val authApi = Network.authApi

    suspend fun getPhoneCode(phone: String, debug: Boolean): Response<Unit>? {
        return try {
            val response = authApi.verifyPhone(phone, debug)
            Log.d("debug", response.code().toString())
            response
        } catch (e: Throwable) {
            Log.d("error", e.message.toString())
            null
        }

    }

    suspend fun performPhoneCheck(phoneWithCode: PhoneWithCode): Response<Unit>? {
        return try {
            val response = authApi.submitPhone(phoneWithCode)
            Log.d("debug", response.code().toString())
            response
        } catch (e: Throwable) {
            Log.d("error", e.message.toString())
            null
        }
    }

    suspend fun changePhone(newPhone: String): Response<Unit>?{
        return try {
            authApi.changePhone(Phone(newPhone))
        }catch (e: Throwable){
            null
        }

    }

    suspend fun getToken(tokenData: TokenData): Response<TokenResponse>? {
        return try {
            val response = authApi.getToken(tokenData)
            if (response.code() == 200) {
                Tokens.token = "Bearer ${response.body()?.access}"
                ProfileId.id = response.body()?.user?.id ?: 0
                response
            } else {
                response
            }
        } catch (e: Throwable) {
            Log.d("debug", e.message.toString())
            if(e is IOException) null
            else{
                try {
                    val response = authApi.getTokenGetError(tokenData)
                    Response.error(-1, response.body()?.detail?.toResponseBody(null)!!)
                }catch (e: Throwable){
                    val response = authApi.getTokenGetErrors(tokenData)
                    Response.error(-1, response.body()?.errors?.keys?.joinToString(",")?.toResponseBody(null)!!)
                }
            }
        }
    }

    suspend fun saveTokenData(phone: String, code: String) {
        withContext(Dispatchers.IO) {
            try {
                val prefs = context.getSharedPreferences(USER_PHONE_CODE, Context.MODE_PRIVATE)
                prefs.edit()
                    .putString(PHONE_KEY, phone)
                    .commit()
                prefs.edit().putString(CODE_KEY, code).commit()

            } catch (e: Throwable) {

            }

        }
    }

    suspend fun getTokenDataFromDisk(): TokenData?{
        return withContext(Dispatchers.IO) {
            val prefs = context.getSharedPreferences(USER_PHONE_CODE, Context.MODE_PRIVATE)
            val phone = prefs.getString(PHONE_KEY, null)
            val code = prefs.getString(CODE_KEY, null)
            if (phone == null || code == null) {
                 return@withContext null
            }
             TokenData(phone, code)
        }

    }

    fun isLogined(): Boolean{
        Log.d("mylog", Tokens.token)
        return Tokens.token.isNotEmpty()
    }

    suspend fun registerNewUser(
        phone: String,
        gender: String,
        photo: String?,
        name: String,
        birthDate: Long,
        about: String
    ): Response<Unit>? {
        Log.d("mylog", "register")
        Log.d("mylog", photo?.toUri().toString())
        return withContext(Dispatchers.IO) {
            val file = File(context.cacheDir, "face.jpg")
            val date = Calendar.getInstance().apply { timeInMillis = birthDate }
            val birthDateString =
                "${date.get(Calendar.YEAR)}-${date.get(Calendar.MONTH)}-${date.get(Calendar.DAY_OF_MONTH)}"
            val map = hashMapOf<String, RequestBody>()
            map["username"] = name.toRequestBody(null)
            map["name"] = name.toRequestBody(null)
            map["gender"] = gender.toRequestBody(null)
            map["about"] = about.toRequestBody(null)
            map["birthday"] = birthDateString.toRequestBody(null)
            if (photo != null) {
                try {
                    context.contentResolver.openInputStream(photo.toUri()).use { input ->
                        file.outputStream().use { output ->
                            input?.copyTo(output)
                        }
                    }

                } catch (e: Throwable) {
                    Log.d("mylog", e.message.toString())
                }

            }
            map["phone"] = phone.toRequestBody(null)
            val part = MultipartBody.Part.createFormData("photo", file.name, file.asRequestBody("image/*".toMediaTypeOrNull()))
            try {
                val response = authApi.registerNewUser(map, if(photo == null) null else part)
                response
            } catch (e: Throwable) {
                Log.d("mylog", e.message.toString())
                null
            }
        }
    }

    fun getUserId(): Long{
        return ProfileId.id
    }

    suspend fun getGcmDevice(token: String): Response<GcmDevice>?{
        return try {
            authApi.getGcmDevice(token)
        }catch (e: Throwable){
            null
        }
    }

    suspend fun createGcmDevice(device: GcmDevice): Response<GcmDevice>?{
        return try {
            authApi.createGcmDevice(device)
        }catch (e: Throwable){
            null
        }
    }

    suspend fun updateGcmDevice(token: String, device: GcmDevice): Response<GcmDevice>?{
        return try {
            authApi.updateGcmDevice(token, device)
        }catch (e: Throwable){
            null
        }
    }

    fun getLocalFcmToken(): String{
        return Tokens.fireBaseToken
    }

    fun setupFcmToken(token: String){
        Tokens.fireBaseToken = token
    }

    suspend fun checkFileSize(uri: Uri): Boolean{
        return withContext(Dispatchers.IO){
            val file = File(context.cacheDir, "photofile.jpg")
            context.contentResolver.openInputStream(uri).use { input ->
                file.outputStream().use { output ->
                    input?.copyTo(output)
                }
            }

            val fileSize =  file.length()/(1024*1024)
            fileSize < 1
        }
    }




    companion object {
        const val USER_PHONE_CODE = "user_phone_code"
        const val PHONE_KEY = "phone_key"
        const val CODE_KEY = "code_key"
    }
}


