package com.bangkit.synco.data.repository

import com.bangkit.synco.data.model.Assesment
import com.bangkit.synco.data.model.AssesmentRequest
import retrofit2.Call
import com.bangkit.synco.data.model.Login
import com.bangkit.synco.data.model.LoginRequest
import com.bangkit.synco.data.model.Register
import com.bangkit.synco.data.model.RegistrationRequest
import com.bangkit.synco.data.model.User
import retrofit2.*
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("auth/login")
    fun doLogin(@Body loginRequest: LoginRequest): Call<Login>

    @Headers("Content-Type: application/json")
    @POST("auth/register")
    fun doRegister(@Body registrationRequest: RegistrationRequest): Call<Register>

    @Headers("Content-Type: application/json")
    @POST("penyakit")
    fun doAssessment(
        @Header("Authorization") auth: String,
        @Body request: AssesmentRequest
    ): Call<Assesment>

    @POST("user/me")
    fun getDataUser(
        @Header("Authorization") auth: String,
    ): Call<User>


}