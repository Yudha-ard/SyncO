package com.bangkit.synco.ui.basicinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.synco.R
import com.bangkit.synco.UserPreferences
import com.bangkit.synco.adapter.HistoryAdapter
import com.bangkit.synco.data.model.HistoryItem
import com.bangkit.synco.data.model.User
import com.bangkit.synco.databinding.ActivityBasicInformationBinding
import com.bangkit.synco.databinding.ActivityHistoryBinding
import com.bangkit.synco.helper.convertStringToDate
import com.bangkit.synco.ui.history.HistoryViewModel
import com.shashank.sony.fancytoastlib.FancyToast
import java.util.Date

class BasicInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBasicInformationBinding
    private lateinit var viewModel : BasicViewModel
    private lateinit var userPref: UserPreferences
    var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBasicInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }
        initView()
        initUser()
    }
    private fun initView() {
        binding?.apply {
            binding.btnSave.setOnClickListener {
                if (validateInput()) {
                    showLoading(true)
                    //ubah dob menjadi type date
                    var dob = convertStringToDate(binding.edtDob.text.toString())
                    var newuser = User(
                        userPref.getLoginData().userId,
                        userPref.getLoginData().firstName,
                        userPref.getLoginData().lastName,
                        dob,
                        binding.edtHeight.text.toString().toInt(),
                        binding.edtWeight.text.toString().toInt(),
                        token,
                        true,
                        userPref.getLoginData().age,
                        userPref.getLoginData().email
                    )
                    viewModel.doUpdate(
                        token,
                        "1",
                        newuser
                    )
                    viewModel.usrUpdate.observe(this@BasicInfoActivity) { result ->
                        Log.d("LoginFragment", "Result is not null: $result")
                        result?.let { user ->
                            showMessage("Update Success")
                        }
                        showLoading(false)
                    }
                }
            }
        }
    }
    private fun initUser(){
        userPref = UserPreferences(this)
        token = userPref.getLoginData().token
    }
    private fun validateInput():Boolean{
        if (binding.edtDob.text.toString().isEmpty()){
            binding.edtDob.error = "Date of Birth tidak boleh kosong"
            binding.edtDob.requestFocus()
            return false
        }
        if (binding.edtHeight.text.toString().isEmpty()){
            binding.edtHeight.error = "Height tidak boleh kosong"
            binding.edtHeight.requestFocus()
            return false
        }
        if (binding.edtWeight.text.toString().isEmpty()){
            binding.edtWeight.error = "Weight tidak boleh kosong"
            binding.edtWeight.requestFocus()
            return false
        }
        return true
    }
    private fun showLoading(isLoading: Boolean) {
        binding?.loading?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showMessage(message: String) {
        FancyToast.makeText(this@BasicInfoActivity, message, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
    }
}