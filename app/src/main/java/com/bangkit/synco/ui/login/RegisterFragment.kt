package com.bangkit.synco.ui.login

import com.bangkit.synco.MainActivity
import com.bangkit.synco.databinding.FragmentRegisterBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class RegisterFragment : Fragment() {
    private var registerFragmentBinding: FragmentRegisterBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registerFragmentBinding = FragmentRegisterBinding.inflate(inflater, container, false)
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
                showLoading(true)
                validateAndRegister()
            }
            btnMove.setOnClickListener {
                (activity as MainActivity).moveToFragment(LoginFragment())
            }
        }
    }

    private fun doRegister() {
        val fname = registerFragmentBinding?.fname?.text.toString().trim()
        val lname = registerFragmentBinding?.lname?.text.toString().trim()
        val email = registerFragmentBinding?.email?.text.toString().trim()
        val password = registerFragmentBinding?.password?.text.toString().trim()

    }

    private fun validateAndRegister() {
        when {
            registerFragmentBinding?.fname?.text.isNullOrBlank() -> {
                registerFragmentBinding?.fname?.error = "first name is required"
                return
            }
            registerFragmentBinding?.lname?.text.isNullOrBlank() -> {
                registerFragmentBinding?.lname?.error = "last name is required"
                return
            }
            registerFragmentBinding?.email?.text.isNullOrBlank() -> {
                registerFragmentBinding?.email?.error = "Username is required"
                return
            }
            registerFragmentBinding?.password?.text.isNullOrBlank() -> {
                registerFragmentBinding?.password?.error = "Password is required"
                return
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        registerFragmentBinding?.loading?.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}
