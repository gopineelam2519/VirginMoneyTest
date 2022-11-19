package com.techblue.virginmoney.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.techblue.virginmoney.api.MainRepositoryImpl
import com.techblue.virginmoney.models.Person
import com.techblue.virginmoney.models.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val mainRepositoryImpl: MainRepositoryImpl) : ViewModel() {

    private val selectedPersonMutableLiveData = MutableLiveData<Person>()
    val selectedPersonLiveData: LiveData<Person> = selectedPersonMutableLiveData

    fun getPersons(): Flow<PagingData<Person>> {
        return mainRepositoryImpl.getPersons().cachedIn(viewModelScope)
    }

    fun getRooms(): LiveData<PagingData<Room>> {
        return mainRepositoryImpl.getRooms().cachedIn(viewModelScope)
    }

    fun setSelectedPerson(person: Person) {
        selectedPersonMutableLiveData.value = person
    }
}