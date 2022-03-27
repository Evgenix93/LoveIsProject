package com.project.loveis.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.repositories.MainRepository
import com.project.loveis.repositories.VideoRepository
import com.project.loveis.util.Gender
import kotlinx.coroutines.launch

class VideoViewModel(app: Application): AndroidViewModel(app) {
    private val videoRepository = VideoRepository(app)
    private val mainRepository = MainRepository(app)
    private val stateLiveData = MutableLiveData<State>()
    val state: LiveData<State>
        get() = stateLiveData

    fun getVideos(){
      viewModelScope.launch{
          val response = videoRepository.getVideos()
         when(response?.code()){
             200 -> stateLiveData.postValue(State.LoadedSingleState(response.body()!!))
             400 -> stateLiveData.postValue(State.ErrorState(400))
         }
      }
    }

    fun getVideoIntent(url: String){
        viewModelScope.launch {
            val intent = videoRepository.getVideoIntent(url)
            if(intent != null)
                stateLiveData.postValue(State.LoadedSingleState(intent))
            else
                stateLiveData.postValue(State.ErrorState(0))
        }
    }
}