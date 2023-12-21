package com.bangkit.synco.ui.profile

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
import com.bangkit.synco.ui.login.RegisterFragment
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
        profileFragmentBinding?.btnLogout?.setOnClickListener {
            Log.d("LogoutFragment", "Logout button clicked")
            (requireActivity() as? MainActivity)?.doLogout()
        }
    }


    private fun initView() {
        profileFragmentBinding?.apply {
            doGetProfile()
            btnBasicInfo.setOnClickListener {
                //intent ke basicInfoActivity
                startActivity(Intent(requireContext(), BasicInfoActivity::class.java))
            }
            btnAssessment.setOnClickListener {
                (activity as MainActivity).moveToFragment(RegisterFragment())
            }
        }
    }
    private fun doGetProfile() {
        val userId = userPref.getLoginData().userId.toString()
        val token = userPref.getLoginData().token
        viewModel.apply {
            showLoading(true)
            doGetProfile(token)
            usrInfo.observe(viewLifecycleOwner) { result ->
                Log.d("UserInfo", "Result is not null: $result")
                profileFragmentBinding?.apply {
                    tvName.text = result.user.firstName
                    tvEmail.text = result.user.email
                    tvAge.text = result.user.age.toString()
                    tvHeight.text = result.user.height.toString()
                    tvWeight.text = result.user.weight.toString()

                }
                _name=result.user.firstName.toString()
                _email=result.user.email.toString()
                _birth=result.user.dob.toString()
                _age=result.user.age.toString()
                _height=result.user.height.toString()
                _weight=result.user.weight.toString()
            }
            message.observe(viewLifecycleOwner) { message ->
                Log.d("UserInfo", "Message is not null: $message")
                profileFragmentBinding?.apply {
                    tvName.text = message
                }
            }
            showLoading(false)
        }
    }

    private fun initVM() {
        viewModel = ViewModelProvider(requireActivity())[ProfileViewModel::class.java]
        viewModel.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
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