package com.techblue.virginmoney.api

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.techblue.virginmoney.models.Person
import com.techblue.virginmoney.models.Room
import com.techblue.virginmoney.paging.PagingDataSource
import com.techblue.virginmoney.paging.RoomsPagingDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val apiService: ApiService) : MainRepository {

    override fun getPersons(): Flow<PagingData<Person>> {
        return Pager(
            config = PagingConfig(
                pageSize = 100,
                prefetchDistance = 2,
            ),
            pagingSourceFactory = {
                PagingDataSource(apiService)
            }
            , initialKey = 1 //this is first Page
        ).flow
    }

    override fun getRooms(): LiveData<PagingData<Room>> {
        return Pager(
            config = PagingConfig(
                pageSize = 100,
                prefetchDistance = 2
            ),
            pagingSourceFactory = {
                RoomsPagingDataSource(apiService)
            }
            , initialKey = 0
        ).liveData
    }

}