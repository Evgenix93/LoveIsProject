package com.project.loveis

sealed class State {
    object LoadingState: State()
    object StartState: State()
    object SuccessState: State()
    class ErrorState(val code: Int): State()
    class LoadedSingleState(val result: Any): State()

}