package com.bangkit.synco.data.model

import com.google.gson.annotations.SerializedName

data class Login(

    @field:SerializedName("loginResult")
    val loginResult: User,

    @field:SerializedName("message")
    val message: String
)

data class LoginRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)