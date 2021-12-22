package com.project.loveis.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.repositories.MainRepository
import kotlinx.coroutines.launch

class VerifyViewModel(app: Application): AndroidViewModel(app) {
    private val repository = MainRepository(app)
    private val stateLiveData = MutableLiveData<State>()
    private var photoUri: Uri? = null
    val state: LiveData<State>
    get() =  stateLiveData

    fun uploadPhoto(uri: Uri){
        stateLiveData.postValue(State.LoadingState)
      viewModelScope.launch {
          val photo = repository.uploadPhoto(uri)
          stateLiveData.postValue(State.LoadedSingleState(photo))
      }
    }

    fun verify(){
      viewModelScope.launch {
          if (photoUri == null)
              return@launch
          stateLiveData.postValue(State.LoadingState)
          when (repository.verify(photoUri!!)?.code()) {
              200 -> stateLiveData.postValue(State.SuccessState)
              400 -> stateLiveData.postValue(State.ErrorState(400))
              410 -> stateLiveData.postValue(State.ErrorState(410))
              null -> stateLiveData.postValue(State.ErrorState(0))
              else -> stateLiveData.postValue(State.ErrorState(2))
          }
      }
    }

     fun safePhotoUri(uri: Uri){
        photoUri = uri
    }
}