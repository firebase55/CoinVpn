package com.A_Developer.CoinVpn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.A_Developer.CoinVpn.adapters.ServerCountryPagerAdapter
import com.google.android.material.tabs.TabLayout

class ServerCountryActivity : AppCompatActivity() {

    private lateinit var tablayoutServerCountry : TabLayout
    private lateinit var viewpagerServerCountry : ViewPager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_country)
        supportActionBar!!.hide()

        initControls()

    }
    private fun initControls() {
        tablayoutServerCountry = findViewById(R.id.tablayout_server_country)
        viewpagerServerCountry = findViewById(R.id.viewpager_server_country)

        viewpagerServerCountry.adapter = ServerCountryPagerAdapter(supportFragmentManager)
        tablayoutServerCountry.setupWithViewPager(viewpagerServerCountry)
    }
}

