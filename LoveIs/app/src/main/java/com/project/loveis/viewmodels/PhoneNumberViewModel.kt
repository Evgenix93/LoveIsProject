package com.project.loveis.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.models.PhoneWithCode
import com.project.loveis.models.TokenData
import com.project.loveis.repositories.AuthRepository
import kotlinx.coroutines.launch

class PhoneNumberViewModel(app: Application): AndroidViewModel(app) {
    private val authRepository = AuthRepository(app)
    private val stateLiveData = MutableLiveData<State>().apply { value = State.StartState }


    val state: LiveData<State>
    get() = stateLiveData

    fun getPhoneCode(phone: String){
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            when (authRepository.getPhoneCode(phone, false)?.code()){
               200 -> stateLiveData.postValue(State.SuccessState)
               404 -> stateLiveData.postValue(State.ErrorState(404))
                408 -> stateLiveData.postValue(State.ErrorState(408))
                409 -> stateLiveData.postValue(State.ErrorState(409))
                400 -> stateLiveData.postValue(State.ErrorState(400))
               null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
           }

        }
    }

    fun performPhoneCheck(phone: String, code: String){
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            when (authRepository.performPhoneCheck(PhoneWithCode(phone, code))?.code()){
                200 -> performAuth(phone, code)
                409 -> stateLiveData.postValue(State.ErrorState(409))
                410 -> performAuth(phone, code)
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }

        }
    }

    fun changePhone(newPhone: String){
        viewModelScope.launch {
            stateLiveData.postValue(State.LoadingState)
            when(authRepository.changePhone(newPhone)?.code()){
                200 -> getPhoneCode(newPhone)
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }
        }

    }

    private fun performAuth(phone: String, code: String){
        Log.d("debug", "performAuth")
        stateLiveData.postValue(State.LoadingState)
        viewModelScope.launch {
            val phoneStr = phone.filter { it != '(' && it != ')' && it != '-' }
            when(authRepository.getToken(TokenData(phoneStr, code))?.code()){
                200 -> {
                    authRepository.saveTokenData(phoneStr, code)
                    stateLiveData.postValue(State.SuccessState)
                }
                401 -> stateLiveData.postValue(State.ErrorState(401))
                400 -> stateLiveData.postValue(State.ErrorState(400))
                null -> stateLiveData.postValue(State.ErrorState(0))
                else -> stateLiveData.postValue(State.ErrorState(2))

            }
        }

    }








}