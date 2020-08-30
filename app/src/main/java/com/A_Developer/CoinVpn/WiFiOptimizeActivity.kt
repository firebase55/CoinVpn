package com.A_Developer.CoinVpn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.A_Developer.CoinVpn.ads.AdManger

class WiFiOptimizeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var linearSpeedTest : LinearLayout
    private lateinit var linearNetworkProtector : LinearLayout

    private val adManager = AdManger()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wi_fi_optimize)
        supportActionBar!!.hide()

        initControls()
    }

    override fun onClick(view: View?) {
        when(view){
            linearSpeedTest -> {
                val intent = Intent(this,HomeActivity::class.java).apply {
                    putExtra("fragment_id",R.id.nav_speed_test)
                }
                adManager.showFBInterstitialAd(this,intent)
                finish()
            }
            linearNetworkProtector -> {
                val intent = Intent(this,NetworkProtectorActivity::class.java)
                adManager.showFBInterstitialAd(this,intent)
                finish()
            }
        }
    }

    private fun initControls(){
        linearSpeedTest = findViewById(R.id.linear_speed_test)
        linearNetworkProtector = findViewById(R.id.linear_network_protector)

        linearSpeedTest.setOnClickListener(this)
        linearNetworkProtector.setOnClickListener(this)
    }
}
