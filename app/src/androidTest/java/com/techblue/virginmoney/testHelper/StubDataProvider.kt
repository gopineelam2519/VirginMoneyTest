package com.techblue.virginmoney.testHelper

import com.techblue.virginmoney.models.Person
import com.techblue.virginmoney.models.Room

object StubDataProvider {
    fun getPersons(): List<Person> {
        return listOf<Person>(
            Person(
                id = "3",
                firstName = "Gopi",
                lastName = "Neelam",
                email = "abc@gmail.com",
                jobtitle = "Android app developer"
            ),
            Person(
                id = "3",
                firstName = "Bharath",
                lastName = "kumar",
                email = "abc2@gmail.com",
                jobtitle = "iOS app developer"
            ),
            Person(
                id = "3",
                firstName = "Sankar",
                lastName = "Mango",
                email = "abc2@gmail.com",
                jobtitle = "Node Js developer"
            )
        )
    }

    fun getRooms(): List<Room> {
        return listOf(
            Room(
                isOccupied = true,
                maxOccupancy = 100,
                id = 1
            ),
            Room(
                isOccupied = false,
                maxOccupancy = 5500,
                id = 2
            ),
            Room(
                isOccupied = false,
                maxOccupancy = 10,
                id = 3
            ),
            Room(
                isOccupied = true,
                maxOccupancy = 1925,
                id = 4
            )
        )
    }
}