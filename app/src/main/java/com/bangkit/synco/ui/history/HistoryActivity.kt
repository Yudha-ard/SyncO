package com.bangkit.synco.ui.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.synco.UserPreferences
import com.bangkit.synco.adapter.HistoryAdapter
import com.bangkit.synco.databinding.ActivityHistoryBinding
import com.shashank.sony.fancytoastlib.FancyToast

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter
    private lateinit var userPref: UserPreferences
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = HistoryAdapter(emptyList())
        userPref = UserPreferences(this)
        viewModel = HistoryViewModel()
        binding.rvHistory.adapter = adapter

        binding.btnBack.setOnClickListener { finish() }
        token = userPref.getToken()
        userPref = UserPreferences(this)
        doHistory()
        Log.d("History User", "$token")
    }

    private fun doHistory() {
        Log.d("History", "$token")
        viewModel.apply {
            doHistory(token)
            history.observe(this@HistoryActivity, Observer { result ->
                Log.d("History", "Result is not null: $result")
                result?.let { history ->
                    val layoutManager = LinearLayoutManager(this@HistoryActivity)
                    binding.rvHistory.layoutManager = layoutManager
                    val itemDecoration = DividerItemDecoration(this@HistoryActivity, layoutManager.orientation)
                    binding.rvHistory.addItemDecoration(itemDecoration)

                    // Update the adapter with the new data
                    adapter.submitList(history)
                }
            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String) {
        FancyToast.makeText(this@HistoryActivity, message, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
    }
}
