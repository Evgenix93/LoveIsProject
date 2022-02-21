package com.project.loveis.viewmodels

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.models.Image
import com.project.loveis.repositories.AuthRepository
import com.project.loveis.repositories.MainRepository
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application): AndroidViewModel(app) {
    private val stateLiveData = MutableLiveData<State>().apply { value = State.StartState }
    private val mainRepository = MainRepository(app)
    private val authRepository = AuthRepository(app)
    private var images = mutableListOf<Image>()
    private var photoNumber = 1

    val state: LiveData<State>
    get() = stateLiveData





    fun getUserInfo(){
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = mainRepository.getCurrentUserInfo()
            when(response?.code()) {
                200 -> {
                    val user = response.body()!!
                    Log.d("Debug", user.images.toString())
                    user.images = user.images.mapIndexed{index, image ->
                        Image(
                            index + 1,
                            image.uuid,
                            image.url
                        )
                    }
                    Log.d("Debug", user.images.toString())
                    images = user.images.toMutableList()
                    stateLiveData.postValue(State.LoadedSingleState(user))
                }
                404 -> stateLiveData.postValue(State.ErrorState(404))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }

    }

    fun updateUserPhoto(fileUri: Uri){
        viewModelScope.launch {
        stateLiveData.postValue(State.LoadingState)
            val response = mainRepository.updateUserPhoto(fileUri)
            when(response?.code()){
                200 -> {stateLiveData.postValue(State.LoadedSingleState(response.body()!!))}
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> {stateLiveData.postValue(State.ErrorState(0))}
                else -> stateLiveData.postValue(State.ErrorState(2))
            }
        }
    }

    fun updateAdditionalPhoto(uri: Uri){
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = mainRepository.updateAdditionalPhoto(uri)
            when(response?.code()){
                200 -> {
                    //val user = response.body()!!
                    //user.images[photoNumber - 1].number = photoNumber
                    //val index = images.indexOfFirst { it.number == photoNumber }
                    //Log.d("mylog", images.toString())
                   // images[index] = user.images[photoNumber - 1]
                   // user.images = images
                    //stateLiveData.postValue(State.LoadedSingleState(user))
                   // Log.d("mylog", user.images.toString())
                    getUserInfo()
                }
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> {stateLiveData.postValue(State.ErrorState(0))}
                else -> stateLiveData.postValue(State.ErrorState(2))
            }
        }
    }

    fun deleteAdditionalPhoto(newPhotoUri: Uri){
        viewModelScope.launch {
            val image = images.firstOrNull{it.number == photoNumber}
            if(image == null) {
                Log.d("mylog", "image null")
                //stateLiveData.postValue(State.LoadedSingleState(Unit))
                updateAdditionalPhoto(newPhotoUri)
            }
            else {
               val response = mainRepository.deleteAdditionalPhoto(image.uuid)
                when(response?.code()){
                    200 -> {
                        updateAdditionalPhoto(newPhotoUri)
                    }
                    400 -> stateLiveData.postValue(State.ErrorState(400))
                    null -> {stateLiveData.postValue(State.ErrorState(0))}
                    else -> stateLiveData.postValue(State.ErrorState(2))
                }
            }
        }
    }



    fun updateUserInfo(name: String, about: String){
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = mainRepository.updateUserInfo(name, about)
            when(response?.code()){
                200 -> stateLiveData.postValue(State.SuccessState)
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }
        }



    }

    fun performAuth(){
        if(authRepository.isLogined()){
            stateLiveData.postValue(State.SuccessState)
            return
        }
        stateLiveData.postValue(State.LoadingState)
        viewModelScope.launch {
            val tokenData = authRepository.getTokenDataFromDisk()
            if(tokenData == null){
                stateLiveData.postValue(State.ErrorState(1))
                return@launch
            }
            when(authRepository.getToken(tokenData)?.code()){
                200 -> stateLiveData.postValue(State.SuccessState)
                401 -> stateLiveData.postValue(State.ErrorState(401))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))

            }
        }
    }

    fun savePhotoNumber(number: Int){
        photoNumber = number
    }







}