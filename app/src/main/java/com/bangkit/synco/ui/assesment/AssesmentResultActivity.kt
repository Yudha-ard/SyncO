package com.bangkit.synco.ui.assesment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.synco.MainActivity
import com.bangkit.synco.R
import com.bangkit.synco.data.model.Assesment
import com.bangkit.synco.databinding.ActivityAssesmentResultBinding
import com.bangkit.synco.ui.home.HomeFragment
import com.bangkit.synco.ui.login.RegisterFragment

class AssesmentResultActivity : AppCompatActivity() {
    private var namaPenyakit: String? = null
    private var deskripsi: String? = null
    private var pencegahan: String? = null
    private lateinit var recyclerView: RecyclerView
    private var binding: ActivityAssesmentResultBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityAssesmentResultBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val assessmentResult = intent.getParcelableExtra<Assesment>("assessmentResult")

        if (assessmentResult != null) {
            namaPenyakit = assessmentResult.namaPenyakit
            deskripsi = assessmentResult.deskripsi
            pencegahan = assessmentResult.pencegahan
        }

        // Check if binding is not null before accessing its properties
        if (binding != null) {
            binding?.diagnose?.text = "You may have " + namaPenyakit
            binding?.infoDiagnose?.text = deskripsi
            val pencegahanList = pencegahan?.split(", ")
            if (pencegahanList != null) {
                val size = pencegahanList.size
                val recyclerView = binding?.recyclerView
                recyclerView?.layoutManager = LinearLayoutManager(this)
                val adapter = PencegahanAdapter(pencegahanList)
                recyclerView?.adapter = adapter
            }
        }

        binding?.btnBack?.setOnClickListener {
            finish()
            val mainActivity = Intent(this, MainActivity::class.java)
            mainActivity.putExtra("fragmentToLoad", "homeFragment") // You can use this extra to specify which fragment to load in MainActivity
            startActivity(mainActivity)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        val mainActivity = Intent(this, MainActivity::class.java)
        mainActivity.putExtra("fragmentToLoad", "homeFragment") // You can use this extra to specify which fragment to load in MainActivity
        startActivity(mainActivity)
    }
}
