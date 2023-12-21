package com.bangkit.synco.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.synco.data.model.ArticleModel
import com.bangkit.synco.data.model.History
import com.bangkit.synco.data.model.HistoryItem
import com.bangkit.synco.data.repository.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel : ViewModel() {
    private val _history = MutableLiveData<List<HistoryItem>>()
    val history: LiveData<List<HistoryItem>> = _history

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun doHistory(token : String) {
        _isLoading.value = false
        ApiConfig.getApiService().getHistory(token)
            .enqueue(object : Callback<History> {
                override fun onResponse(call: Call<History>, response: Response<History>) {
                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()
                        _history.value = apiResponse?.history
                        _message.value = "Successful for article"
                    } else {
                        _message.value = "Failed to get articles: ${response.errorBody()?.string()}"
                    }
                    _isLoading.value = true
                }

                override fun onFailure(call: Call<History>, t: Throwable) {
                    _message.value = "Error getting articles: ${t.localizedMessage}"
                    _isLoading.value = true
                }
            })
    }
}

data class ErrorResponse(
    val error: Boolean,
    val message: String
)