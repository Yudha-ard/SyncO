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
    private val _articles = MutableLiveData<List<ArticleModel>>()
    val articles: LiveData<List<ArticleModel>> = _articles

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun doArticle(page: Int) {
        _isLoading.value = false
        ApiConfig.getApiService().getAllArticles()
            .enqueue(object : Callback<List<ArticleModel>> {
                override fun onResponse(
                    call: Call<List<ArticleModel>>,
                    response: Response<List<ArticleModel>>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val apiResponse = response.body()
                        // Assuming _articles is MutableLiveData<List<ArticleModel>>
                        _articles.value = apiResponse ?: emptyList()
                        _message.value = "Successful for article"
                    } else {
                        _message.value = "Failed to get articles: ${response.errorBody()?.string()}"
                    }
                    _isLoading.value = true
                }

                override fun onFailure(call: Call<List<ArticleModel>>, t: Throwable) {
                    _message.value = "Error getting articles: ${t.localizedMessage}"
                    _isLoading.value = true
                }
            })
    }


    data class ErrorResponse(
        val error: Boolean,
        val message: String
    )
}