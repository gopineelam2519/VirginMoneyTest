package com.techblue.virginmoney.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Room(
    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null,

    @SerializedName("isOccupied")
    @Expose
    var isOccupied: Boolean = false,

    @SerializedName("maxOccupancy")
    @Expose
    var maxOccupancy: Int? = null,

    @SerializedName("id")
    @Expose
    var id: Int = 0
)