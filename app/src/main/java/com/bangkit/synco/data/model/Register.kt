package com.bangkit.synco.data.model

import com.google.gson.annotations.SerializedName

data class Register(
    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String,

)

data class RegistrationRequest(
    @SerializedName("first_name")
    val firstName: String,

    @SerializedName("last_name")
    val lastName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
