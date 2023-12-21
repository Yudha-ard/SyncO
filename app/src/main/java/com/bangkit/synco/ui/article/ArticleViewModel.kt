package com.bangkit.synco.ui.article

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.synco.data.model.ArticleModel
import com.bangkit.synco.data.repository.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel : ViewModel() {
    private val _articles = MutableLiveData<ArticleModel>()
    val articles: LiveData<ArticleModel> = _articles

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun doArticle(page: Int) {
        _isLoading.value = false
        ApiConfig.getApiService().getArticles()
            .enqueue(object : Callback<ArticleModel> {
                override fun onResponse(call: Call<ArticleModel>, response: Response<ArticleModel>) {
                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()
                        _articles.value = apiResponse?.let { it }!!
                        _message.value = "Successful for article"
                    } else {
                        _message.value = "Failed to get articles: ${response.errorBody()?.string()}"
                    }
                    _isLoading.value = true
                }

                override fun onFailure(call: Call<ArticleModel>, t: Throwable) {
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