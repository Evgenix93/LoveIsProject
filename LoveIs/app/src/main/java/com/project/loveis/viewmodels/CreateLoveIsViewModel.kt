package com.project.loveis.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.repositories.MainRepository
import kotlinx.coroutines.launch

class CreateLoveIsViewModel(app: Application): AndroidViewModel(app) {
    private val repository = MainRepository(app)
    private val stateLiveData = MutableLiveData<State>(State.StartState)
    val state: LiveData<State>
    get() = stateLiveData

    fun createLoveIs(
        type: Int,
        place: Int,
        date: String,
        telegramUrl:String,
        whatsApp:String,
        userId:Int
    ){
        stateLiveData.postValue(State.LoadingState)
        viewModelScope.launch {
          val response =  repository.createLoveIs(
              type,
              place,
              date,
              telegramUrl,
              whatsApp,
              userId
          )

            if (response == null)
                stateLiveData.postValue(State.ErrorState(0))
            else
                when(response.code()){
                    200 -> stateLiveData.postValue(State.SuccessState)
                    400 -> stateLiveData.postValue(State.ErrorState(400))
                    404 -> stateLiveData.postValue(State.ErrorState(404))
                    409 -> stateLiveData.postValue(State.ErrorState(409))
                }
        }
    }

    fun getPlaces(){
        viewModelScope.launch{
            val response = repository.getPlaces()
            if (response == null)
                stateLiveData.postValue(State.ErrorState(0))
            else{
                when (response.code()){
                    200 -> stateLiveData.postValue(State.LoadedSingleState(response.body()!!))
                    400 -> stateLiveData.postValue(State.ErrorState(400))
                }
            }
        }
    }

    fun clearState(){
        stateLiveData.postValue(State.StartState)
    }
}