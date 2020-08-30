package com.A_Developer.CoinVpn

import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {

    private lateinit var webview : WebView
    private lateinit var relLoadingData : RelativeLayout
    private lateinit var lblLoading : TextView

    private var isPrivacyPolicy : Boolean? = null
    private var linkToShow : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)
        supportActionBar!!.hide()

        initControls()
        fillData()

        webview.webViewClient = object : WebViewClient(){
            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                relLoadingData.visibility = View.GONE
            }
        }
    }

    private fun initControls() {
        webview = findViewById(R.id.webview)
        relLoadingData = findViewById(R.id.rel_loading_data)
        lblLoading = findViewById(R.id.lbl_loading)

        isPrivacyPolicy = intent.getBooleanExtra("is_privacy_policy",true)
        linkToShow = intent.getStringExtra("link_to_show")
    }

    private fun fillData() {
        if(isPrivacyPolicy!!){
            supportActionBar!!.title = "Privacy Policy"
            lblLoading.text = "Loading Privacy Policy"
        } else {
            supportActionBar!!.title = "Terms of service"
            lblLoading.text = "Loading terms of service"
        }

        webview.apply {
            webChromeClient = WebChromeClient()
            loadUrl(linkToShow)
        }
        webview.settings.apply {
            builtInZoomControls = true
            displayZoomControls = true
            javaScriptEnabled = true
        }
    }
}
