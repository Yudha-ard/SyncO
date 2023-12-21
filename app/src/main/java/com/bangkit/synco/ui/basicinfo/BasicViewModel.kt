package com.bangkit.synco.ui.basicinfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.synco.data.model.User
import com.bangkit.synco.data.model.UserProfile
import com.bangkit.synco.data.repository.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BasicViewModel : ViewModel(){
    private val _usrUpdate = MutableLiveData<User>()
    val usrUpdate: LiveData<User> = _usrUpdate

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun doUpdate(token:String, userId:String, user: User) {
        _isLoading.value = false
        ApiConfig.getApiService().updateProfile(token, userId, user)
            .enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                    Log.d("LoginFragment", "ini hasil ${"Response Body: ${Gson().toJson(response.body())}"}")
                    if (response.isSuccessful && response.body() != null) {
                        _isLoading.value = true
                        _usrUpdate.value = response.body()?.user

                        // Log the actual response body
                        Log.d("LoginFragment", "Response Body: ${Gson().toJson(response.body())}")

                    } else {
                        _message.value = "Login failed: ${response.errorBody()?.string()}"
                    }
                }

                override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                    _message.value = "Login error: ${t.localizedMessage}"
                    _isLoading.value = false
                }
            })
    }
}

data class ErrorResponse(
    val error: Boolean,
    val message: String
)