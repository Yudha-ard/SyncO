package com.bangkit.synco.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bangkit.synco.MainActivity
import com.bangkit.synco.R
import com.bangkit.synco.databinding.FragmentAuthBinding


class AuthFragment : Fragment() {
    private var binding: FragmentAuthBinding? = null
    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.cardView?.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
        binding?.btnLogin?.setOnClickListener {
            (activity as? MainActivity)?.moveToLoginFragment()
        }

        binding?.btnRegister?.setOnClickListener {
            (activity as? MainActivity)?.moveToRegisterFragment()
        }

        (activity as MainActivity).supportActionBar?.hide()

    }


}

