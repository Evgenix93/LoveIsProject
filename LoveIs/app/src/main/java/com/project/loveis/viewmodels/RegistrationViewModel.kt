package com.project.loveis.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.project.loveis.State
import com.project.loveis.repositories.AuthRepository
import kotlinx.coroutines.launch

class RegistrationViewModel(app: Application) : AndroidViewModel(app) {
    private val authRepository = AuthRepository(app)
    private val stateLiveData = MutableLiveData<State>()

    val state: LiveData<State>
        get() = stateLiveData

    fun registerNewUser(
        phone: String,
        gender: String,
        photo: String?,
        name: String,
        birthDate: Long,
        about: String
    ) {
        stateLiveData.postValue(State.LoadingState)
        viewModelScope.launch {
            when (authRepository.registerNewUser(
                phone = phone,
                gender = gender,
                photo = photo,
                name = name,
                birthDate = birthDate,
                about = about
            )?.code()) {
                200 -> stateLiveData.postValue(State.SuccessState)
                null -> stateLiveData.postValue(State.ErrorState(0))
                400 -> {
                    stateLiveData.postValue(State.ErrorState(400))
                }
                409 -> stateLiveData.postValue(State.ErrorState(409))
                else -> stateLiveData.postValue(State.ErrorState(2))
            }
        }

    }
}