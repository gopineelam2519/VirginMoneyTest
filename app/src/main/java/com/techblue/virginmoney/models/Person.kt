package com.techblue.virginmoney.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("createdAt")
    @Expose
    var createdAt: String? = null,

    @SerializedName("firstName")
    @Expose
    var firstName: String? = null,

    @SerializedName("avatar")
    @Expose
    var avatar: String? = null,

    @SerializedName("lastName")
    @Expose
    var lastName: String? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null,

    @SerializedName("jobtitle")
    @Expose
    var jobtitle: String? = null,

    @SerializedName("favouriteColor")
    @Expose
    var favouriteColor: String? = null,

    @SerializedName("id")
    @Expose
    var id: String? = null
)