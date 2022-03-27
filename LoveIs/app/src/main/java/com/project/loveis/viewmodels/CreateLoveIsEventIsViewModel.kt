package com.project.loveis.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.models.Image
import com.project.loveis.repositories.LoveIsEventIsRepository
import com.project.loveis.repositories.MainRepository
import com.project.loveis.repositories.PlaceRepository
import kotlinx.coroutines.launch

class CreateLoveIsEventIsViewModel(app: Application): AndroidViewModel(app) {
    private val loveIsEventIsRepository = LoveIsEventIsRepository()
    private val placeRepository = PlaceRepository(app)
    private val mainRepository = MainRepository(app)
    private val stateLiveData = MutableLiveData<State>(State.StartState)
    val state: LiveData<State>
    get() = stateLiveData

    fun createLoveIs(
        type: Int,
        place: Int,
        date: String,
        telegramUrl:String,
        whatsApp:String,
        userId:Long
    ){
        stateLiveData.postValue(State.LoadingState)
        viewModelScope.launch {
          val response =  loveIsEventIsRepository.createLoveIs(
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
                    500 -> stateLiveData.postValue(State.ErrorState(500))
                }
        }
    }

    fun createEventIs(
        type: Int,
        place: Int,
        date: String,
        price: Int,
        personCount: Int,
        telegramUrl:String,
        whatsAppUrl:String
    ){
        stateLiveData.postValue(State.LoadingState)
        viewModelScope.launch {
          val response = loveIsEventIsRepository.createEventIs(
              type,
              place,
              price,
              date,
              personCount,
              telegramUrl,
              whatsAppUrl
          )
            when(response?.code()){
                200 -> stateLiveData.postValue(State.SuccessState)
                400 -> stateLiveData.postValue(State.ErrorState(400))
                404 -> stateLiveData.postValue(State.ErrorState(404))
                409 -> stateLiveData.postValue(State.ErrorState(409))
                500 -> stateLiveData.postValue(State.ErrorState(500))
                null -> stateLiveData.postValue(State.ErrorState(0))
            }
        }
    }

    fun getPlaces(){
        viewModelScope.launch{
            val response = placeRepository.getPlaces()
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

    fun getCurrentUser(){
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
                        stateLiveData.postValue(State.LoadedCurrentUser(user))
                    }
                    404 -> stateLiveData.postValue(State.ErrorState(404))
                    null -> stateLiveData.postValue(State.ErrorState(0))
                    else -> stateLiveData.postValue(State.ErrorState(2))
                }

            }


    }
}