package com.project.loveis.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.project.loveis.apis.LoveIsEventIsApi
import com.project.loveis.models.LoveIs
import java.lang.Integer.max

/*class LoveIsPagingSource(val source: LoveIsEventIsApi): PagingSource<Int, LoveIs>() {
    override fun getRefreshKey(state: PagingState<Int, LoveIs>): Int? {

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LoveIs> {
        val currentKey = params.key ?: STARTING_KEY

        val result = source.getLoveIsMeetings(currentKey, 25, )

    }

    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)

    companion object{
        const val STARTING_KEY = 0
    }

}*/