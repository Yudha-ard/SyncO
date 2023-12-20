package com.bangkit.synco.ui.login

import com.bangkit.synco.MainActivity
import com.bangkit.synco.databinding.FragmentRegisterBinding
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.synco.UserPreferences
import com.bangkit.synco.data.model.RegistrationRequest
import com.bangkit.synco.ui.home.HomeFragment
import com.shashank.sony.fancytoastlib.FancyToast

class RegisterFragment : Fragment() {
    private var registerFragmentBinding: FragmentRegisterBinding? = null
    private lateinit var authVM: AuthViewModel
    private lateinit var userPref: UserPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerFragmentBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        initVM()
        userPref = UserPreferences(requireContext())
        return registerFragmentBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        registerFragmentBinding = null
    }

    private fun initView() {
        registerFragmentBinding?.apply {
            btnAction.setOnClickListener {
                showMessage("Registering")
                showLoading(true)
                validateAndRegister()
            }
            btnMove.setOnClickListener {
                (activity as MainActivity).moveToFragment(LoginFragment())
            }
            btnBack.setOnClickListener {
                (activity as MainActivity).moveToFragment(AuthFragment())
            }
        }
    }

    private fun doRegister() {
        val fname = registerFragmentBinding?.fname?.text.toString().trim()
        val lname = registerFragmentBinding?.lname?.text.toString().trim()
        val email = registerFragmentBinding?.email?.text.toString().trim()
        val password = registerFragmentBinding?.password?.text.toString().trim()

        val registrationRequest = RegistrationRequest(fname, lname, email, password)
        Log.d("doRegiterfragment","$fname")
        authVM.doRegister(registrationRequest)
    }


    private fun initVM() {
        authVM = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        authVM.isLoading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let { showLoading(it) }
        }

        authVM.registrationSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                (activity as MainActivity).moveToFragment(AuthFragment())
            }else{
                (activity as MainActivity).moveToFragment(AuthFragment())
            }
        }
        authVM.message.observe(viewLifecycleOwner) { message ->
            message?.let { showMessage(it) }
        }
    }

    private fun validateAndRegister() {
        when {
            registerFragmentBinding?.email?.text.isNullOrBlank() -> {
                registerFragmentBinding?.email?.error = "Username is required"
                return
            }
            registerFragmentBinding?.password?.text.isNullOrBlank() -> {
                registerFragmentBinding?.password?.error = "Password is required"
                return
            }
        }
        doRegister()
    }

    private fun showLoading(isLoading: Boolean) {
        registerFragmentBinding?.loading?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String) {
        FancyToast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
