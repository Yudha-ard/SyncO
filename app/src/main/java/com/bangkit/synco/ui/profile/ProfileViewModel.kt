package com.bangkit.synco.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.synco.data.model.UserProfile
import com.bangkit.synco.data.repository.ApiConfig
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel : ViewModel(){
    private val _usrInfo = MutableLiveData<UserProfile>()
    val usrInfo: LiveData<UserProfile> = _usrInfo

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    data class ErrorResponse(
        val error: Boolean,
        val message: String
    )

    fun doGetProfile(auth: String) {
        _isLoading.value = true
        ApiConfig.getApiService().getProfile(auth)
            .enqueue(object : Callback<UserProfile> {
                override fun onResponse(call: Call<UserProfile>, response: Response<UserProfile>) {
                    if (response.isSuccessful && response.body() != null) {
                        _isLoading.value = false
                        _usrInfo.value = response.body()
                    }
                }

                override fun onFailure(call: Call<UserProfile>, t: Throwable) {
                    _message.value = "Profile error: ${t.localizedMessage}"
                    _isLoading.value = false
                }
            })
    }
}