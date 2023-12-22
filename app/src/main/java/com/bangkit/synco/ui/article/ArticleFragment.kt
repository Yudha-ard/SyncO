package com.bangkit.synco.ui.article

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.synco.MainActivity
import com.bangkit.synco.UserPreferences
import com.bangkit.synco.adapter.ArticleAdapter
import com.bangkit.synco.data.model.ArticleModel
import com.bangkit.synco.databinding.FragmentArticleBinding
import com.bangkit.synco.ui.webview.WebViewActivity
import com.shashank.sony.fancytoastlib.FancyToast

class ArticleFragment : Fragment() {
    private var articleFragmentBinding: FragmentArticleBinding? = null
    private lateinit var viewModel: ArticleViewModel
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var userPref: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        articleFragmentBinding = FragmentArticleBinding.inflate(inflater, container, false)
        userPref = UserPreferences(requireContext())
        return articleFragmentBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Hide the action bar
        (activity as MainActivity).supportActionBar?.hide()

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(ArticleViewModel::class.java)

        val layoutManager = LinearLayoutManager(requireContext())
        articleAdapter = ArticleAdapter(emptyList())
        articleFragmentBinding?.rvArticle?.layoutManager = layoutManager
        articleFragmentBinding?.rvArticle?.adapter = articleAdapter

        viewModel.articles.observe(viewLifecycleOwner) { articles ->
            articles?.let {
                articleAdapter.submitList(articles)
            }
        }


        viewModel.message.observe(viewLifecycleOwner) { message ->
            showMessage(message)
        }

        // Load articles when the fragment is first created
        if (viewModel.articles.value == null) {
            doArticle()
        }
    }

    private fun doArticle() {
        val page = 1
        viewModel.doArticle(page)
    }

    private fun showLoading(isLoading: Boolean) {
        articleFragmentBinding?.loading?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String) {
        // Handle error messages here
    }

    override fun onDetach() {
        super.onDetach()
        articleFragmentBinding = null
    }
}