package com.project.loveis

import com.project.loveis.models.LoveIs
import com.project.loveis.models.MeetingFilterType
import com.project.loveis.models.User

sealed class State {
    object LoadingState: State()
    object StartState: State()
    object SuccessState: State()
    class ErrorState(val code: Int): State()
    class LoadedSingleState(val result: Any): State()
    class LoveIsMeetingsLoadedState(val meetings: List<LoveIs>, val type: MeetingFilterType): State()
    class LoadedCurrentUserId(val id: Long): State()
    class LoadedCurrentUser(val user: User): State()

}