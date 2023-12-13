package com.bangkit.synco.ui.history

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.synco.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        val extra = intent.extras
        val date = extra?.getString("date").orEmpty()
        val symptom = extra?.getString("symptom").orEmpty()
    }
}
