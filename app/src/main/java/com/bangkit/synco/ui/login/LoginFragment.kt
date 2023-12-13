package com.bangkit.synco.ui.login

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.synco.MainActivity
import com.bangkit.synco.UserPreferences
import com.bangkit.synco.data.model.LoginRequest
import com.bangkit.synco.data.model.RegistrationRequest
import com.bangkit.synco.data.model.User
import com.bangkit.synco.databinding.FragmentLoginBinding
import com.bangkit.synco.ui.home.HomeFragment
import com.shashank.sony.fancytoastlib.FancyToast

class LoginFragment : Fragment() {
    private var loginFragmentBinding: FragmentLoginBinding? = null
    private lateinit var authVM: AuthViewModel
    private lateinit var userPref: UserPreferences
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginFragmentBinding = FragmentLoginBinding.inflate(inflater, container, false)
        initVM()
        userPref = UserPreferences(requireContext())
        return loginFragmentBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        initView()
    }

    private fun initView() {
        loginFragmentBinding?.apply {
            btnAction.setOnClickListener {
                showLoading(true)
                showMessage("Logging in, please wait")
                validateAndLogin()
            }
            btnMove.setOnClickListener {
                (activity as MainActivity).moveToFragment(RegisterFragment())
            }

            btnBack.setOnClickListener {
                (activity as MainActivity).moveToFragment(AuthFragment())
            }
        }
    }
    private fun doLogin() {
        val email = loginFragmentBinding?.email?.text.toString().trim()
        val password = loginFragmentBinding?.password?.text.toString().trim()
        val loginRequest = LoginRequest(email, password)
        authVM.apply {
            doLogin(loginRequest)
            userLogin.observe(viewLifecycleOwner) { result ->
                Log.d("LoginFragment", "Result is not null: $result")
                Log.d("LoginFragment", "firstName: ${result.loginResult.firstName}")
                Log.d("LoginFragment", "lastName: ${result.loginResult.lastName}")
                val currentUser = User(
                    result.loginResult.userId,
                    null,
                    null,
                    null,
                    null,
                    null,
                     result.loginResult.token,
                    true
                )

                Log.d("LoginFragment", "doLogin: $currentUser")
                userPref.setUserLogin(currentUser)

                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Login Successfully")
                    setPositiveButton("Ok") { _, _ ->
                        (activity as MainActivity).moveToFragment(HomeFragment())
                    }
                    create()
                    show()
                }

            }
        }
    }

    private fun initVM() {
        authVM = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        authVM.isLoading.observe(viewLifecycleOwner) { showLoading(it) }
        authVM.message.observe(viewLifecycleOwner) { showMessage(it) }
    }
    private fun validateAndLogin() {
        when {
            loginFragmentBinding?.email?.text!!.isBlank() -> {
                loginFragmentBinding?.email?.error = "Username is required"
                return
            }
            loginFragmentBinding?.password?.text!!.isBlank() -> {
                loginFragmentBinding?.password?.error = "Password is required"
                return
            }
        }
        doLogin()
    }

    private fun showLoading(isLoading: Boolean) {
        loginFragmentBinding?.loading?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showMessage(message: String) {
        FancyToast.makeText(requireContext(), message, FancyToast.LENGTH_SHORT, FancyToast.INFO, false).show()
    }

    override fun onDetach() {
        super.onDetach()
        loginFragmentBinding = null
    }
}
