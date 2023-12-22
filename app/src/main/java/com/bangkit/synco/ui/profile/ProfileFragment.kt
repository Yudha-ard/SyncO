package com.bangkit.synco.ui.profile

import com.bangkit.synco.ui.history.HistoryActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.synco.MainActivity
import com.bangkit.synco.UserPreferences
import com.bangkit.synco.databinding.FragmentProfileBinding
import com.bangkit.synco.ui.basicinfo.BasicInfoActivity
import com.shashank.sony.fancytoastlib.FancyToast


class ProfileFragment : Fragment() {
    private var profileFragmentBinding: FragmentProfileBinding? = null
    private lateinit var viewModel : ProfileViewModel
    private lateinit var userPref: UserPreferences
    private var _name=""
    private var _email=""
    private var _age=""
    private var _birth=""
    private var _height=""
    private var _weight=""
    private var _token=""
    private var _userId=""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileFragmentBinding = FragmentProfileBinding.inflate(inflater, container, false)
        initVM()
        userPref = UserPreferences(requireContext())
        return profileFragmentBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileFragmentBinding?.apply {
            btnLogout?.setOnClickListener {
                Log.d("LogoutFragment", "Logout button clicked")
                (requireActivity() as? MainActivity)?.doLogout()
            }
            doGetProfile()
            btnBasicInfo.setOnClickListener {
                //intent ke basicInfoActivity
                startActivity(Intent(requireContext(), BasicInfoActivity::class.java))
            }
            btnAssessment.setOnClickListener {

                startActivity(Intent(requireContext(), HistoryActivity::class.java))
            }
        }
    }

    private fun doGetProfile() {
        val userId = userPref.getLoginData().userId
        val token = userPref.getToken()
        Log.d("ini Token", "$token")
        userPref.setToken(token)
        viewModel.apply {
            doGetProfile(token)
            usrInfo.observe(viewLifecycleOwner) { result ->
                profileFragmentBinding?.apply {
                    val firstName = result.user.firstName
                    val lastName = result.user.lastName
                    val Email = result.user.email
                    tvName.text =  "$firstName $lastName"


                    tvEmail.text =  "$Email"
                    if(result.user.age != null){
                        tvAge.text = result.user.age.toString()
                    }else{
                        tvAge.text = "--"
                    }
                    if(result.user.height != null){
                        tvHeight.text = result.user.height.toString()
                    }else{
                        tvHeight.text = "--"
                    }
                    if(result.user.weight != null){
                        tvWeight.text = result.user.weight.toString()
                    }else{
                        tvWeight.text = "--"
                    }
                }
                _birth=result.user.dob.toString()
                _age=result.user.age.toString()
                _height=result.user.height.toString()
                _weight=result.user.weight.toString()
            }
            showLoading(false)
        }
    }

    private fun initVM() {
        viewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        viewModel.message.observe(viewLifecycleOwner) { showMessage(it) }
    }

    private fun showLoading(isLoading: Boolean) {
        profileFragmentBinding?.loading?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String) {
        FancyToast.makeText(requireContext(), message, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
    }

    override fun onDetach() {
        super.onDetach()
        profileFragmentBinding = null
    }
}