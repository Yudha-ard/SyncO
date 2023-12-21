package com.bangkit.synco.ui.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.synco.UserPreferences
import com.bangkit.synco.adapter.HistoryAdapter
import com.bangkit.synco.data.model.ArticleModel
import com.bangkit.synco.data.model.HistoryItem
import com.bangkit.synco.databinding.ActivityHistoryBinding
import com.bangkit.synco.ui.webview.WebViewActivity
import com.shashank.sony.fancytoastlib.FancyToast

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel : HistoryViewModel
    private lateinit var adapter: HistoryAdapter
    private lateinit var userPref: UserPreferences
    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        initView()
        initUser()
    }
    private fun initView() {
        binding?.apply {
            doHistory()
        }
    }
    private fun initUser(){
        userPref = UserPreferences(this)
        token = userPref.getLoginData().token
    }
    private fun doHistory() {
        viewModel.apply {
            showLoading(true)
            doHistory(token)
            history.observe(this@HistoryActivity) { result ->
                Log.d("LoginFragment", "Result is not null: $result")
                result?.let { history ->
                    val layoutManager = LinearLayoutManager(this@HistoryActivity)
                    binding.rvHistory.layoutManager = layoutManager
                    val itemDecoration = DividerItemDecoration(this@HistoryActivity, layoutManager.orientation)
                    binding.rvHistory.addItemDecoration(itemDecoration)
                    adapter = HistoryAdapter(history)
                    binding.rvHistory.adapter = adapter
                    adapter.setOnItemClickListener(object : HistoryAdapter.OnItemClickListener {
                        override fun onItemClick(item: HistoryItem) {
                            Toast.makeText(this@HistoryActivity, "Click ${item.nama_penyakit}", Toast.LENGTH_SHORT).show()
                        }
                    })
                    showLoading(false)
                }
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding?.loading?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String) {
        FancyToast.makeText(this@HistoryActivity, message, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
    }
}
