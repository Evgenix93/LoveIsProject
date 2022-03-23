package com.project.loveis.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.models.MeetingFilterType
import com.project.loveis.repositories.AuthRepository
import com.project.loveis.repositories.LoveIsEventIsRepository
import com.project.loveis.repositories.MainRepository
import com.project.loveis.util.MeetingStatus
import kotlinx.coroutines.launch

class LoveIsEveintIsViewModel(app: Application): AndroidViewModel(app) {
    private val loveIsRepository = LoveIsEventIsRepository()
    private val authRepository = AuthRepository(app)
    private val mainRepository = MainRepository(app)
    private val stateLiveData = MutableLiveData<State>(State.StartState)

    val state: LiveData<State>
    get() = stateLiveData

    fun getLoveIsMeetings(page: Int = 1, size: Int = 25, type: MeetingFilterType){
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.getLoveIsMeetings(page, size, type)
            when (response?.code()){
                200 -> {
                    val loveIsList = when(type){
                        MeetingFilterType.ACTIVE -> response.body()!!.list.filter { it.status != "create" }
                        MeetingFilterType.INCOMING -> response.body()!!.list.filter { it.status == "create" }
                        else -> response.body()!!.list
                    }
                    stateLiveData.postValue(State.LoveIsMeetingsLoadedState(loveIsList, type))
                }
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }



        }

    }

    fun getEventIsMeetings(page: Int = 1, size: Int = 25, type: MeetingFilterType){
        Log.d("debug", "getEvents")
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.getEventIsMeetings(page, size, type)
            when (response?.code()){
                200 -> {
                    val eventIsList = response.body()!!.list

                    stateLiveData.postValue(State.EventIsMeetingsLoadedState(eventIsList, type))
                }
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }

    fun getCurrentUserId(){
        stateLiveData.postValue(State.LoadedCurrentUserId(authRepository.getUserId()))
    }

    fun getCurrentUserInfo(){
        val user = mainRepository.getCurrentUser()
        user ?: return
        stateLiveData.postValue(State.LoadedCurrentUser(user))
    }

    fun completeLoveIs(id: Long){
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.completeLoveIs(id)
            when (response?.code()){
                200 -> {
                      stateLiveData.postValue(State.SuccessState)
                    }


                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }

    fun changeLoveIsStatus(id: Long, status: MeetingStatus){
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.changeLoveIsStatus(id, status)
            when (response?.code()){
                200 -> {
                    stateLiveData.postValue(State.SuccessState)
                }


                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }

    fun acceptLoveIs(id: Long){
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            val response = loveIsRepository.acceptLoveIs(id)
            when (response?.code()){
                200 -> {
                    stateLiveData.postValue(State.SuccessState)
                }


                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }
}