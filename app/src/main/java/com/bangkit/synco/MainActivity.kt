package com.bangkit.synco

import com.bangkit.synco.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.bangkit.synco.ui.article.ArticleFragment
import com.bangkit.synco.ui.home.HomeFragment
import com.bangkit.synco.ui.login.AuthFragment
import com.bangkit.synco.ui.login.LoginFragment
import com.bangkit.synco.ui.login.RegisterFragment
import com.bangkit.synco.ui.profile.ProfileFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var loginFragment: LoginFragment
    private lateinit var registerFragment: RegisterFragment
    private lateinit var homeFragment: HomeFragment
    private lateinit var authFragment: AuthFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize fragments
        authFragment = AuthFragment()
        loginFragment = LoginFragment()
        registerFragment = RegisterFragment()
        homeFragment = HomeFragment()

        supportActionBar?.hide()
        val isLoggedIn = true
        showFragment(authFragment)

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    moveToFragment(HomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    moveToFragment(ProfileFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_article -> {
                    moveToFragment(ArticleFragment())
                    return@setOnItemSelectedListener true
                }
                else -> return@setOnItemSelectedListener false
            }
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()
        updateNavigationBarVisibility(false)
    }

    fun moveToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commitNow()
        updateNavigationBarVisibility(true)
    }

    private fun updateNavigationBarVisibility(isLoggedIn: Boolean) {
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        if (isLoggedIn) {
            bottomAppBar.visibility = View.VISIBLE
        } else {
            bottomAppBar.visibility = View.GONE
        }
    }

    fun moveToLoginFragment() {
        moveToFragment(LoginFragment())
        updateNavigationBarVisibility(false)
    }

    fun moveToRegisterFragment() {
        moveToFragment(RegisterFragment())
        updateNavigationBarVisibility(false)
    }
}

