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
import com.bangkit.synco.UserPreferences
import com.bangkit.synco.data.model.User
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

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        usrPref = UserPreferences(requireContext())

        Log.d("homeFragment", usrPref.getFirstName())

        val firstName = usrPref.getFirstName()
        val lastName = usrPref.getLastName()
        if ((firstName != "" || firstName != null) && (lastName != "" || lastName != null)) {
            Log.d("homeFragment", "harusnya gak null$firstName//$lastName//")
            binding?.tvHalo?.text = "Hello $firstName $lastName"
        } else {
            fetchUserData()
            val firstName = usrPref.getFirstName()
            val lastName = usrPref.getLastName()
            if (firstName != null){
                binding?.tvHalo?.text = "Hello"
            }
            binding?.tvHalo?.text = "Hello, $firstName $lastName"
        }
        binding?.buttonStart?.setOnClickListener {
            val intent = Intent(activity, AssessmentActivity::class.java)
            startActivity(intent)
        }

        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun fetchUserData() {
        val authToken = "${usrPref.getLoginData().token}"
        if (authToken != null) {
            val apiService = ApiConfig.getApiService()
            apiService.getDataUser("Bearer $authToken").enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()
                        if (user != null) {
                            val firstName = user.firstName ?: ""
                            val lastName = user.lastName ?: ""
                            usrPref.updateUsername(firstName, lastName)
                            _message.value = "update name: ${response.body()}"
                            Log.d("homeFragment", "Response Body: ${Gson().toJson(response.body())}")

                        }
                    } else {
                        _message.value = "updatefailed: ${response.message()}"
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                }
            })
        } else {
        }
    }
}
