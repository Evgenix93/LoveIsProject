package com.project.loveis.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.loveis.State
import com.project.loveis.repositories.MainRepository
import com.project.loveis.repositories.PlaceRepository
import kotlinx.coroutines.launch

class CreatePlaceViewModel(app: Application): AndroidViewModel(app) {
    private val repository =  PlaceRepository(app)
    private val mainRepository = MainRepository(app)
    private val stateLiveData = MutableLiveData<State>()
    private var photoUri: Uri? = null
    val state: LiveData<State>
        get() = stateLiveData


    fun openPhoto(uri: Uri){
        stateLiveData.postValue(State.LoadingState)
        viewModelScope.launch {
            val photo = mainRepository.uploadPhoto(uri)
            stateLiveData.postValue(State.LoadedSingleState(photo))
        }
    }

    fun createPlace(name: String, address: String){

        viewModelScope.launch {
            if (photoUri == null)
                return@launch
            stateLiveData.postValue(State.LoadingState)
           val response = repository.createPlace(name, address, photoUri!!)
            if (response == null)
                stateLiveData.postValue(State.ErrorState(0))
            else{
                when(response.code()){
                    200 -> stateLiveData.postValue(State.SuccessState)
                    400 -> stateLiveData.postValue(State.ErrorState(400))
                }
            }
        }
    }

    fun safePhotoUri(uri: Uri){
        photoUri = uri
    }
}