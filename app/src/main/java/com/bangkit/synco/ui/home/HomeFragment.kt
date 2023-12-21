package com.bangkit.synco.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.synco.UserPreferences
import com.bangkit.synco.data.model.ArticleModel
import com.bangkit.synco.data.repository.ApiConfig
import com.bangkit.synco.databinding.FragmentHomeBinding
import com.bangkit.synco.ui.assesment.AssessmentActivity
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private lateinit var usrPref: UserPreferences
    private lateinit var itemArticleAdapter: ItemArticleAdapter

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _articles = MutableLiveData<List<ArticleModel>>() // Use List<ArticleModel>
    val articles: LiveData<List<ArticleModel>> = _articles

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        usrPref = UserPreferences(requireContext())

        itemArticleAdapter = ItemArticleAdapter(emptyList()) // Initialize with an empty list


        loadArticles()
        binding?.buttonStart?.setOnClickListener {
            val intent = Intent(activity, AssessmentActivity::class.java)
            startActivity(intent)
        }

        _articles.observe(viewLifecycleOwner, Observer { articles ->
            itemArticleAdapter.updateArticles(articles)
        })

        loadArticles()
        binding?.recyclerView?.apply {

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = itemArticleAdapter
            Log.d("homeFragment", "masuk")
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun loadArticles() {
        Log.d("homeFragment", "masuk loadArticle ")
        ApiConfig.getApiService().getAllArticles().enqueue(object : Callback<List<ArticleModel>> {
            override fun onResponse(call: Call<List<ArticleModel>>, response: Response<List<ArticleModel>>) {
                val statusCode = response.code() // Get the HTTP status code
                val responseBody = response.body()?.toString() ?: "null"
                Log.d("homeFragment", "Response from server: $statusCode $responseBody")
                if (response.isSuccessful && response.body() != null) {
                    val articleList = response.body()
                    _articles.postValue(articleList)
                    _message.value = "Articles loaded successfully"
                    Log.d("homeFragment", "Successful article response: ${Gson().toJson(response.body())}")
                } else {
                    _message.value = "Failed to get articles. Status Code: $statusCode"
                    Log.d("homeFragment", "Failed article response: ${Gson().toJson(response.body())}")
                }
            }

            override fun onFailure(call: Call<List<ArticleModel>>, t: Throwable) {
                _message.value = "Error getting articles: ${t.localizedMessage}"
            }

        })
    }

}
