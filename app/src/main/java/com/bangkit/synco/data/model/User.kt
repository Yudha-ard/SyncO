package com.bangkit.synco.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class User(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("first_name")
    val firstName: String?,

    @field:SerializedName("last_name")
    val lastName: String?,

    @field:SerializedName("dateofbirth")
    val dob: Date?,

    @field:SerializedName("height")
    val height: Int?,

    @field:SerializedName("weight")
    val weight: Int?,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("isLogin")
    val isLogin: Boolean,

    @field:SerializedName("age")
    val age: Int?,
    @field:SerializedName("email")
    val email: String?,
)