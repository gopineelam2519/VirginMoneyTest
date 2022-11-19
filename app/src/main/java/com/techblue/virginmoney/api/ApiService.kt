package com.techblue.virginmoney.api

import com.techblue.virginmoney.models.Person
import com.techblue.virginmoney.models.Room
import retrofit2.http.GET


interface ApiService {

    @GET("people")
    suspend fun getPersons(): List<Person>

    @GET("rooms")
    suspend fun getRooms(): List<Room>
}