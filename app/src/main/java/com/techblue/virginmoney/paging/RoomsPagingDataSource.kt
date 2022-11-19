package com.techblue.virginmoney.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.techblue.virginmoney.Constants
import com.techblue.virginmoney.api.ApiService
import com.techblue.virginmoney.models.Room

class RoomsPagingDataSource constructor(private val apiService: ApiService) : PagingSource<Int, Room>() {

    override fun getRefreshKey(state: PagingState<Int, Room>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Room> {
        return try {
            val position = params.key ?: 0
            val response = apiService.getRooms()

            val prevKey = if (position == 0) null else position
            val nextKey = if (position + Constants.PAGE_SIZE >= response.size) response.size else position + Constants.PAGE_SIZE

            LoadResult.Page(
                data = response.subList(position, if (position + Constants.PAGE_SIZE >= response.size) response.size else position + Constants.PAGE_SIZE), prevKey = prevKey,
                nextKey = if (nextKey == response.size) null else nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}