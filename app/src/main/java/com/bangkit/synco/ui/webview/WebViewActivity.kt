package com.bangkit.synco.ui.webview

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.synco.databinding.ActivityWebViewBinding


class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            webView.settings.javaScriptEnabled = true
            webView.loadUrl("https://www.halodoc.com/kesehatan/asma")
            webView.webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView, title: String) {
                    tvTitle.text = title
                }
            }

            btnBack.setOnClickListener { finish() }
        }
    }
}
