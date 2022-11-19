package com.techblue.virginmoney.listeners

interface NavigationListener {
    fun navigateToPersonsPage()
    fun navigateToRoomsPage()
    fun navigateToPersonDetailsPage()
    fun showProgress()
    fun hideProgress()
}