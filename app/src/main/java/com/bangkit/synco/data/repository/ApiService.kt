package com.bangkit.synco.data.repository

import com.bangkit.synco.data.model.ArticleModel
import com.bangkit.synco.data.model.Assesment
import com.bangkit.synco.data.model.AssesmentRequest
import com.bangkit.synco.data.model.History
import retrofit2.Call
import com.bangkit.synco.data.model.Login
import com.bangkit.synco.data.model.LoginRequest
import com.bangkit.synco.data.model.Register
import com.bangkit.synco.data.model.RegistrationRequest
import com.bangkit.synco.data.model.User
import com.bangkit.synco.data.model.UserProfile
import retrofit2.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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

    @Headers("Content-Type: application/json")
    @GET("/api/api/users/me")
    fun getProfile(
        @Header("Authorization") authorization: String,
    ): Call<UserProfile>

    @Headers("Content-Type: application/json")
    @GET("/api/users/age/{userId}")
    fun getAge(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String
    ): Call<User>

    @Headers("Content-Type: application/json")
    @GET("/api/users/height/{userId}")
    fun getHeight(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String
    ): Call<User>

    @Headers("Content-Type: application/json")
    @GET("/api/users/weight/{userId}")
    fun getWeight(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String
    ): Call<User>

    @Headers("Content-Type: application/json")
    @PUT("api/users/update/me")
    fun updateProfile(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Body user: User
    ): Call<UserProfile>

    @Headers("Content-Type: application/json")
    @GET("/artikel")
    fun getArticles(
    ): Call<ArticleModel>
    @GET("artikel")
    @Headers("Content-Type: application/json")
    fun getAllArticles(
    ): Call<List<ArticleModel>>


    @Headers("Content-Type: application/json")
    @GET("/api/users/history/me")
    fun getHistory(@Header("Authorization") authorization: String): Call<History>


}