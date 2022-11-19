package com.techblue.virginmoney.testHelper

import com.techblue.virginmoney.models.Person
import com.techblue.virginmoney.models.Room

object StubDataProvider {
    fun getPersons(): List<Person> {
        val list = mutableListOf<Person>()
        for (i in 1..25) {
            list.add(
                Person(
                    id = "$i",
                    firstName = "Gopi_$i",
                    lastName = "Neelam_$i",
                    email = "abc_$i@gmail.com",
                    jobtitle = "Android app developer_$i"
                )
            )
        }
        return list
    }

    fun getRooms(): List<Room> {
        val list = mutableListOf<Room>()

        for (i in 1..25) {
            list.add(
                Room(
                    isOccupied = i % 2 == 0,
                    maxOccupancy = 10 * i,
                    id = i
                )
            )
        }
        return list
    }
}