package com.bangkit.synco.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bangkit.synco.MainActivity
import com.bangkit.synco.R
import com.bangkit.synco.databinding.FragmentAuthBinding
import com.bangkit.synco.databinding.FragmentProfileBinding
import com.bangkit.synco.ui.login.LoginFragment
import com.bangkit.synco.ui.login.RegisterFragment

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var binding: FragmentProfileBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.btnLogout?.setOnClickListener {
            Log.d("LogoutFragment", "Logout button clicked")
            (requireActivity() as? MainActivity)?.doLogout()
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}