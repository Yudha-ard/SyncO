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
    private lateinit var viewModel : ArticleViewModel
    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var userPref: UserPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        articleFragmentBinding = FragmentArticleBinding.inflate(inflater, container, false)
        initVM()
        userPref = UserPreferences(requireContext())
        return articleFragmentBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        initView()
    }

    private fun initView() {
        articleFragmentBinding?.apply {
            doArticle()
        }
    }
    private fun doArticle() {
        val page = 1
        viewModel.apply {
            doArticle(page)
            showLoading(true)
            articles.observe(viewLifecycleOwner) { result ->
                Log.d("LoginFragment", "Result is not null: $result")
                result?.let { articles ->
                    val layoutManager = LinearLayoutManager(requireContext())
                    articleFragmentBinding?.rvArticle?.layoutManager = layoutManager
                    val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
                    articleFragmentBinding?.rvArticle?.addItemDecoration(itemDecoration)
                    articleAdapter = ArticleAdapter(articles)
                    articleFragmentBinding?.rvArticle?.adapter = articleAdapter
                    articleAdapter.notifyDataSetChanged()
                    //listener
                    articleAdapter.setOnItemClickListener(object : ArticleAdapter.OnItemClickListener {
                        override fun onItemClick(item: ArticleModel) {
                            val intent = Intent(requireActivity(), WebViewActivity::class.java)
                            intent.putExtra("link",item.link)
                            startActivity(intent)
                        }

                    })
                    showLoading(false)
                }
            }
        }
    }

    private fun initVM() {
        viewModel = ViewModelProvider(requireActivity())[ArticleViewModel::class.java]
    }

    private fun showLoading(isLoading: Boolean) {
        articleFragmentBinding?.loading?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String) {
        FancyToast.makeText(requireContext(), message, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
    }

    override fun onDetach() {
        super.onDetach()
        articleFragmentBinding = null
    }
}