package com.bangkit.synco.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.synco.data.model.Login
import com.bangkit.synco.data.model.LoginRequest
import com.bangkit.synco.data.model.Register
import com.bangkit.synco.data.model.RegistrationRequest
import com.bangkit.synco.data.model.User
import com.bangkit.synco.data.repository.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.mindrot.jbcrypt.BCrypt
import retrofit2.http.Body

class AuthViewModel : ViewModel(){
    private val _usrLogin = MutableLiveData<Login>()
    val userLogin: LiveData<Login> = _usrLogin

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun doLogin(loginRequest: LoginRequest) {
        _isLoading.value = false
        ApiConfig.getApiService().doLogin(loginRequest)
            .enqueue(object : Callback<Login> {
                override fun onResponse(call: Call<Login>, response: Response<Login>) {
                    Log.d("LoginFragment", "ini hasil ${"Response Body: ${Gson().toJson(response.body())}"}")
                    if (response.isSuccessful && response.body() != null) {
                        _isLoading.value = true
                        _usrLogin.value = response.body()
                        _isLoading.value = true
                        _usrLogin.value = response.body()
                        _message.value = "Login successful for ${response.body()?.loginResult?.firstName}"

                        // Log the actual response body
                        Log.d("LoginFragment", "Response Body: ${Gson().toJson(response.body())}")

                        // Log specific fields for debugging
                        Log.d("LoginFragment", "Response firstName: ${response.body()?.loginResult?.firstName}")
                        Log.d("LoginFragment", "Response lastName: ${response.body()?.loginResult?.lastName}")
                    } else {
                        _message.value = "Login failed: ${response.errorBody()?.string()}"
                    }
                }

                override fun onFailure(call: Call<Login>, t: Throwable) {
                    _message.value = "Login error: ${t.localizedMessage}"
                    _isLoading.value = false
                }
            })
    }

    fun doRegister(registrationRequest: RegistrationRequest) {
        _isLoading.value = false
        ApiConfig.getApiService().doRegister(registrationRequest)
            .enqueue(object : Callback<Register> {
                override fun onResponse(
                    call: Call<Register>,
                    response: Response<Register>
                ) {
                    if (response.isSuccessful) {
                        _isLoading.value = true
                        _message.value = "Registration successful. Name: ${registrationRequest.firstName}, Email: ${registrationRequest.email}"
                    } else {
                        try {
                            val errorBody = response.errorBody()?.string()
                            val errorMessage = Gson().fromJson(errorBody, ErrorResponse::class.java)
                            _message.value = errorMessage.message
                        } catch (e: Exception) {
                            _message.value = "Registration failed: ${response.message()}"
                        }
                    }
                }

                override fun onFailure(call: Call<Register>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = t.message
                }
            })
    }
}

data class ErrorResponse(
    val error: Boolean,
    val message: String
)