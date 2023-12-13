package com.bangkit.synco

import com.bangkit.synco.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.util.Log
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
    private  var binding: ActivityMainBinding? = null
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var userPref: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        userPref = UserPreferences(this)
        supportActionBar?.hide()

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

        checkSession()
    }

    fun moveToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .commitNow()
        updateNavigationBarVisibility(userPref.getLoginData().isLogin)
    }

    private fun checkSession() {
        val isLoggedIn = userPref.getLoginData().isLogin
        if (isLoggedIn) {
            moveToFragment(HomeFragment())
        } else {
            moveToFragment(AuthFragment())
        }
        updateNavigationBarVisibility(isLoggedIn)
    }

    fun doLogout() {
        userPref.logout()
        moveToFragment(LoginFragment())
        updateNavigationBarVisibility(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun updateNavigationBarVisibility(isLoggedIn: Boolean) {
        val bottomAppBar = findViewById<BottomAppBar>(R.id.bottomAppBar)
        Log.d("MainActivity", "isLoggedIn: $isLoggedIn")
        if (isLoggedIn) {
            bottomAppBar.visibility = View.VISIBLE

        } else {
            bottomAppBar.visibility = View.GONE

        }
    }
}

