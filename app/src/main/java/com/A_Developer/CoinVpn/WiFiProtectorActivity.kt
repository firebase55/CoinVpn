package com.A_Developer.CoinVpn

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.A_Developer.CoinVpn.ads.AdManger
import com.A_Developer.CoinVpn.helpers.HydraSdkHelper

class WiFiProtectorActivity : AppCompatActivity() {

    private lateinit var imgWifiProtector : ImageView
    private lateinit var lblWifiStatus : TextView
    private lateinit var lblWifiStatusDesc : TextView
    private lateinit var btnSecureWifi : Button
    private lateinit var lblWifiSSID : TextView
    private lateinit var lblIpAddress : TextView
    private lateinit var lblLinkSpeed : TextView

    private var adManager = AdManger()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wi_fi_protector)
        supportActionBar!!.hide()

        initControls()
        fillData()


        btnSecureWifi.setOnClickListener {
            val intent = Intent(this@WiFiProtectorActivity,ServerCountryActivity::class.java)
            adManager.showFBInterstitialAd(this,intent)
            finish()
        }
    }

    private fun initControls() {
        imgWifiProtector = findViewById(R.id.img_wifi_protector)
        lblWifiStatus = findViewById(R.id.lbl_wifi_status)
        lblWifiStatusDesc = findViewById(R.id.lbl_wifi_status_desc)
        btnSecureWifi = findViewById(R.id.btn_secure_wifi)
        lblWifiSSID = findViewById(R.id.lbl_wifi_ssid)
        lblIpAddress = findViewById(R.id.lbl_ip_address)
        lblLinkSpeed = findViewById(R.id.lbl_link_speed)
    }

    private fun fillData(){
        if(HydraSdkHelper().isConnected()){
            lblWifiStatus.apply{
                text = "protected"
            }
            imgWifiProtector.setImageResource(R.drawable.protected_wifi)
            lblWifiStatusDesc.text = "Your Wi-Fi network and data are secured by ${getString(R.string.app_name)} from online threats."
            btnSecureWifi.text = "Show more"
        } else {
            lblWifiStatus.apply {
                text = "Unprotected"
            }
            imgWifiProtector.setImageResource(R.drawable.unprotected_wifi)
            lblWifiStatusDesc.text = "Turn on the VPN to secure your data and protect yourself from online threats."
            btnSecureWifi.text = "secure wi-fi"
        }
        val wifiManger = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        lblLinkSpeed.text = ""+wifiManger.connectionInfo.linkSpeed+"mbps"
        lblWifiSSID.text = wifiManger.connectionInfo.ssid
        lblIpAddress.text = Formatter.formatIpAddress(wifiManger.connectionInfo.ipAddress)
    }
}
