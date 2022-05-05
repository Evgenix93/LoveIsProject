package com.project.loveis.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class SplashScreenViewModel(app: Application): AndroidViewModel(app) {
    private val authRepository = AuthRepository(app)
    private val stateLiveData = MutableLiveData<State>()
    val state: LiveData<State>
        get() = stateLiveData

    fun performAuth() {
        viewModelScope.launch {
            delay(1500)
            if (authRepository.isLogined()) {
                Log.d("mylog", "isLogined")
                stateLiveData.postValue(State.SuccessState)
                return@launch
            }
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

    private suspend fun getErrorFromResponse(response: ResponseBody): String{
        return withContext(Dispatchers.IO) {
            response.string()
        }
    }
}