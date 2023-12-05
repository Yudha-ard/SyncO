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
import com.bangkit.synco.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var loginFragmentBinding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        loginFragmentBinding = FragmentLoginBinding.inflate(inflater, container, false)

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
                validateAndLogin()
            }
            btnMove.setOnClickListener {
                (activity as MainActivity).moveToFragment(RegisterFragment())
            }
        }
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
    }

    private fun showLoading(isLoading: Boolean) {
        loginFragmentBinding?.loading?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    override fun onDetach() {
        super.onDetach()
        loginFragmentBinding = null
    }
}
