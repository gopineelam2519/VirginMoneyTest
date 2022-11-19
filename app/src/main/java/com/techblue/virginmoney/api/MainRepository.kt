package com.techblue.virginmoney.api

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.techblue.virginmoney.models.Person
import com.techblue.virginmoney.models.Room
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getPersons(): Flow<PagingData<Person>>
    fun getRooms(): LiveData<PagingData<Room>>
}