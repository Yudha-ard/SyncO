package com.bangkit.synco.ui.webview

import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.synco.databinding.ActivityWebViewBinding


class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding
    private var link = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        link = intent.getStringExtra("link").toString()
        with(binding) {
            webView.settings.javaScriptEnabled = true
            webView.loadUrl(link.toString())
            webView.webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView, title: String) {
                    tvTitle.text = title
                }
            }

            btnBack.setOnClickListener { finish() }
        }
    }
}
