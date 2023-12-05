package com.bangkit.synco.ui.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.synco.R
import com.bangkit.synco.databinding.ActivitySplashScreenBinding
import java.util.*

@SuppressLint("CustomSplashScreen")
class  SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content for the splash screen
        setContentView(R.layout.activity_splash_screen)

        // Hide the status bar and navigation bar
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)

    }
}