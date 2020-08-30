package com.A_Developer.CoinVpn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_loading)

        loadingActivity = this
    }

    override fun onBackPressed() {

    }

    companion object{
        var loadingActivity : LoadingActivity? = null
    }
}
