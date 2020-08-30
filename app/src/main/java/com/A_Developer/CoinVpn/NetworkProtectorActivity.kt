package com.A_Developer.CoinVpn

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Handler
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.A_Developer.CoinVpn.adapters.NetworkStatusAdapter
import com.A_Developer.CoinVpn.ads.AdManger
import com.A_Developer.CoinVpn.pojos.NetworkStatus

class NetworkProtectorActivity : AppCompatActivity() {

    private lateinit var lblWifiSSID : TextView
    private lateinit var lstNetworkStatus : ListView

    private val listOfNetworkStatus = mutableListOf<NetworkStatus>()
    private var networkStatusAdapter : NetworkStatusAdapter? = null
    private val handler = Handler()
    private var position = 0
    private val adManager = AdManger()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network_protector)
        supportActionBar!!.hide()

        initControls()
        makeList()
        fillData()

    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(networkStatusCheck)
    }

    private fun initControls(){
        lblWifiSSID = findViewById(R.id.lbl_wifi_ssid)
        lstNetworkStatus = findViewById(R.id.lst_network_status)
    }

    private fun makeList(){
        listOfNetworkStatus.add(
            NetworkStatus(
                false,
                "Reading network information"
            )
        )
        listOfNetworkStatus.add(
            NetworkStatus(
                false,
                "Checking DNS hijacking"
            )
        )
        listOfNetworkStatus.add(
            NetworkStatus(
                false,
                "Checking SSL certificate"
            )
        )
        listOfNetworkStatus.add(
            NetworkStatus(
                false,
                "Real time protection enabled"
            )
        )
        listOfNetworkStatus.add(
            NetworkStatus(
                false,
                "Smart lock enabled"
            )
        )
    }

    private fun fillData() {
        val wifiManger = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        lblWifiSSID.text = wifiManger.connectionInfo.ssid
        networkStatusAdapter = NetworkStatusAdapter(listOfNetworkStatus,this)
        lstNetworkStatus.adapter = networkStatusAdapter
        handler.postDelayed(networkStatusCheck,2000)
    }

    private var networkStatusCheck = object : Runnable{
        override fun run() {
            lstNetworkStatus.smoothScrollToPosition(position)
            val networkStatus = listOfNetworkStatus[position]
            networkStatus.isCompleteCheck = true
            listOfNetworkStatus.removeAt(position)
            listOfNetworkStatus.add(position,networkStatus)
            networkStatusAdapter!!.notifyDataSetChanged()
            position++
            if(position < networkStatusAdapter!!.count){
                handler.postDelayed(this,2000)
            } else {
                val intent = Intent(this@NetworkProtectorActivity,WiFiProtectorActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
