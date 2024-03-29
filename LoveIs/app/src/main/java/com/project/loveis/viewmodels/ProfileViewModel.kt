package com.project.loveis.viewmodels

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.messaging.FirebaseMessaging
import com.project.loveis.State
import com.project.loveis.models.*
import com.project.loveis.repositories.AuthRepository
import com.project.loveis.repositories.LoveIsEventIsRepository
import com.project.loveis.repositories.MainRepository
import com.project.loveis.repositories.SubsriptionRepository
import com.project.loveis.util.CloudMessageType
import com.project.loveis.util.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ProfileViewModel(app: Application) : AndroidViewModel(app) {
    private val stateLiveData = MutableLiveData<State>().apply { value = State.StartState }
    private val mainRepository = MainRepository(app)
    private val authRepository = AuthRepository(app)
    private val loveIsRepository = LoveIsEventIsRepository()
    private val subscriptionRepository = SubsriptionRepository()
    private var images = mutableListOf<Image>()
    private var photoNumber = 1
    var subscription = false
        private set

    val state: LiveData<State>
        get() = stateLiveData


    fun getUserInfo() {
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = mainRepository.getCurrentUserInfo()
            when (response?.code()) {
                200 -> {
                    val user = response.body()!!
                    mainRepository.setUpCurrentUser(user)
                    Log.d("Debug", user.images.toString())
                    user.images = user.images.mapIndexed { index, image ->
                        Image(
                            index + 1,
                            image.uuid,
                            image.url
                        )
                    }
                    Log.d("Debug", user.images.toString())
                    images = user.images.toMutableList()
                    subscription = user.subscription != null
                    stateLiveData.postValue(State.LoadedSingleState(user))
                }
                404 -> stateLiveData.postValue(State.ErrorState(404))
                null -> stateLiveData.postValue(State.ErrorState(0))
                -1 -> stateLiveData.postValue(State.ErrorMessageState(getErrorFromResponse(response.errorBody()!!)))
                else -> stateLiveData.postValue(State.ErrorMessageState("getUserInfo: код ошибки ${response.code()}"))
            }

        }

    }

    fun updateUserPhoto(fileUri: Uri) {
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = mainRepository.updateUserPhoto(fileUri)
            when (response?.code()) {
                200 -> {
                    stateLiveData.postValue(State.LoadedSingleState(response.body()!!))
                }
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> {
                    stateLiveData.postValue(State.ErrorState(0))
                }
                -1 -> stateLiveData.postValue(State.ErrorMessageState(getErrorFromResponse(response.errorBody()!!)))
                else -> stateLiveData.postValue(State.ErrorMessageState("updateUserPhoto: код ошибки ${response.code()}"))


            }
        }
    }

    private fun addAdditionalPhoto(uri: Uri) {
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = mainRepository.updateAdditionalPhoto(uri)
            when (response?.code()) {
                200 -> {
                    getUserInfo()
                }
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> {
                    stateLiveData.postValue(State.ErrorState(0))
                }
                -1 -> stateLiveData.postValue(State.ErrorMessageState(getErrorFromResponse(response.errorBody()!!)))
                else -> stateLiveData.postValue(State.ErrorMessageState("addAdditionalPhoto: код ошибки ${response.code()}"))

            }
        }
    }



    fun updateAdditionalPhoto(newPhotoUri: Uri) {
        viewModelScope.launch {
            val image = images.firstOrNull { it.number == photoNumber }
            if (image == null) {
                Log.d("mylog", "image null")
                addAdditionalPhoto(newPhotoUri)
            } else {
                val response = mainRepository.deleteAdditionalPhoto(image.uuid)
                when (response?.code()) {
                    200 -> {
                        addAdditionalPhoto(newPhotoUri)
                    }
                    400 -> stateLiveData.postValue(State.ErrorState(400))
                    null -> {
                        stateLiveData.postValue(State.ErrorState(0))
                    }
                    -1 -> stateLiveData.postValue(State.ErrorMessageState(getErrorFromResponse(response.errorBody()!!)))
                    else -> stateLiveData.postValue(State.ErrorMessageState("updateAdditionalPhoto: код ошибки ${response.code()}"))

                }
            }
        }
    }

    fun deleteAdditionalPhoto(){
        viewModelScope.launch {
            val image = images.firstOrNull { it.number == photoNumber }
            if(image != null) {
                val response = mainRepository.deleteAdditionalPhoto(image.uuid)
                when (response?.code()) {
                    200 -> { getUserInfo()}
                    400 -> stateLiveData.postValue(State.ErrorState(400))
                    null -> {
                        stateLiveData.postValue(State.ErrorState(0))
                    }
                    -1 -> stateLiveData.postValue(State.ErrorMessageState(getErrorFromResponse(response.errorBody()!!)))
                    else -> stateLiveData.postValue(State.ErrorMessageState("updateAdditionalPhoto: код ошибки ${response.code()}"))
                }
            }
        }
    }


    fun updateUserInfo(name: String, about: String) {
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = mainRepository.updateUserInfo(name, about)
            when (response?.code()) {
                200 -> stateLiveData.postValue(State.SuccessState)
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                -1 -> stateLiveData.postValue(State.ErrorMessageState(getErrorFromResponse(response.errorBody()!!)))
                else -> stateLiveData.postValue(State.ErrorMessageState("updateUserInfo: код ошибки ${response.code()}"))

            }
        }


    }

    fun performAuth() {
        if (authRepository.isLogined()) {
            Log.d("mylog", "isLogined")
            stateLiveData.postValue(State.SuccessState)
            return
        }
        stateLiveData.postValue(State.LoadingState)
        viewModelScope.launch {
            val tokenData = authRepository.getTokenDataFromDisk()
            if (tokenData == null) {
                stateLiveData.postValue(State.ErrorState(1))
                return@launch
            }
            val response = authRepository.getToken(tokenData)
            when (response?.code()) {
                200 -> stateLiveData.postValue(State.SuccessState)
                401 -> stateLiveData.postValue(State.ErrorState(401))
                null -> stateLiveData.postValue(State.ErrorState(0))
                -1 -> stateLiveData.postValue(State.ErrorMessageState(getErrorFromResponse(response.errorBody()!!)))
                else -> stateLiveData.postValue(State.ErrorMessageState("performAuth: код ошибки ${response.code()}"))


            }
        }
    }

    fun savePhotoNumber(number: Int) {
        photoNumber = number
    }

    fun getLoveIsMeetings(page: Int = 1, size: Int = 25, type: MeetingFilterType) {
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.getLoveIsMeetings(page, size, type)
            when (response?.code()) {
                200 -> {
                    val loveIsList = when (type) {
                        MeetingFilterType.ACTIVE -> response.body()!!.list
                        MeetingFilterType.INCOMING -> response.body()!!.list.filter { it.status == "create" }
                        else -> response.body()!!.list
                    }
                    stateLiveData.postValue(State.LoveIsMeetingsLoadedState(loveIsList, type))
                }
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                -1 -> stateLiveData.postValue(State.ErrorMessageState(getErrorFromResponse(response.errorBody()!!)))
                else -> stateLiveData.postValue(State.ErrorMessageState("getLoveIsMeetings: код ошибки ${response.code()}"))

            }


        }

    }

    fun updateCoordinates(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            //stateLiveData.postValue(State.LoadingState)
            val response = mainRepository.updateCoordinates(
                Coordinates(
                    latitude = latitude.toString(),
                    longitude = longitude.toString(),
                    null
                )
            )
            when (response?.code()) {
                200 -> {}
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                -1 -> stateLiveData.postValue(State.ErrorMessageState(getErrorFromResponse(response.errorBody()!!)))
                else -> stateLiveData.postValue(State.ErrorMessageState("updateCoordinates: код ошибки ${response.code()}"))

            }


        }

    }

    fun sendFcmToken(){
        viewModelScope.launch {
            if(authRepository.getLocalFcmToken().isNotEmpty())
                return@launch
            val token = getFcmToken()
            token ?: return@launch
            authRepository.setupFcmToken(token)
            if(checkFcmToken(token) == false){
                authRepository.createGcmDevice(GcmDevice(token, CloudMessageType.FCM.name))
            }else{
                authRepository.updateGcmDevice(token, GcmDevice(token, CloudMessageType.FCM.name))
            }
        }

    }

    private suspend fun getFcmToken(): String? {
        return suspendCoroutine {
            FirebaseMessaging.getInstance().token
                .addOnSuccessListener { token ->
                    it.resume(token)

                }
                .addOnFailureListener { _ ->
                    it.resume(null)

                }
        }
    }

    private suspend fun checkFcmToken(token: String): Boolean?{
        val response = authRepository.getGcmDevice(token)
        return when(response?.code()){
            200 -> true
            500 -> {
                stateLiveData.postValue(State.ErrorMessageState("500 Internal Server Error"))
                null
            }
            else -> {
                //stateLiveData.postValue(State.ErrorMessageState("checkToken error code: ${response?.code()}"))
                false

            }
        }
    }

    private suspend fun getErrorFromResponse(response: ResponseBody): String{
        return withContext(Dispatchers.IO) {
            response.string()
        }
    }

    fun confirmSubscription(value: Int){
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US)
                .apply { timeZone = TimeZone.getDefault() }
            val date = Date(
                2023 - 1900,
                 2,
               10,
                0,
                0
            )
            val dateString = dateFormat.format(date)
            Log.d("MyDebug", "date = $dateString")
            val response = subscriptionRepository.confirmSubsription(SubsriptionRequestData(value, dateString))
            when (response?.code()) {
                204 -> {
                    stateLiveData.postValue(State.SuccessState)
                }

                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }

    fun logout(){
        viewModelScope.launch {
            authRepository.deleteGcmDevice()
            authRepository.deleteTokenData()
            stateLiveData.postValue(State.SuccessState)
        }
    }

}