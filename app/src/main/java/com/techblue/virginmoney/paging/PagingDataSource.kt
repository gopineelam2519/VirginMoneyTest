package com.techblue.virginmoney.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.techblue.virginmoney.Constants
import com.techblue.virginmoney.api.ApiService
import com.techblue.virginmoney.models.Person

class PagingDataSource constructor(private val apiService: ApiService) : PagingSource<Int, Person>() {
    override fun getRefreshKey(state: PagingState<Int, Person>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        return try {
            //consider each page contains 10 records
            val currentPage = params.key ?: 1
            //get all persons list from GET call
            val response = apiService.getPersons()

            //for 1st page pass null
            val prevKey = if (currentPage == 1) null else currentPage

            LoadResult.Page(
                //from full list creating the subList
                data = response.subList((currentPage - 1) * Constants.PAGE_SIZE, if (currentPage * Constants.PAGE_SIZE > response.size) response.size else (currentPage * Constants.PAGE_SIZE)),
                prevKey = prevKey,
                nextKey = if (currentPage * Constants.PAGE_SIZE > response.size) null else currentPage + 1 // if it reaches the end then pass next key as null then it won't call again
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}