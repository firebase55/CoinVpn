package com.A_Developer.CoinVpn

import android.os.Bundle
import android.text.format.Formatter
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.A_Developer.CoinVpn.helpers.HydraSdkHelper
import com.A_Developer.CoinVpn.helpers.SharedPrefHelper
import com.A_Developer.CoinVpn.pojos.CommonClass
import java.util.*

class DisconnectDetailsActivity : AppCompatActivity() {

    private lateinit var lblCountryFlag : TextView
    private lateinit var lblCountryName : TextView
    private lateinit var relDay : RelativeLayout
    private lateinit var lblDay : TextView
    private lateinit var relHour : RelativeLayout
    private lateinit var lblhour : TextView
    private lateinit var relMinute : RelativeLayout
    private lateinit var lblMinute : TextView
    private lateinit var relSecond : RelativeLayout
    private lateinit var lblSecond : TextView
    private lateinit var linearDataUsage : LinearLayout
    private lateinit var lblUsedSize : TextView
    private lateinit var lblLimitSize : TextView
    private lateinit var linearUnlimited : LinearLayout

    private var countryName : String? = null
    private var isUnlimited : Boolean? = null
    private var trafficUsed : Long? = null
    private var trafficLimit : Long? = null

    private var hydraSdkHelper = HydraSdkHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disconnect_details)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initControls()

        hydraSdkHelper.remainingTraffic {isUnlimited, trafficUsed, trafficLimit ->
            this.isUnlimited = isUnlimited
            this.trafficUsed = trafficUsed
            this.trafficLimit = trafficLimit
            fillData()
        }
    }

    private fun initControls() {
        lblCountryFlag = findViewById(R.id.lbl_country_flag)
        lblCountryName = findViewById(R.id.lbl_country_name)
        relDay = findViewById(R.id.rel_day)
        lblDay = findViewById(R.id.lbl_day)
        relHour = findViewById(R.id.rel_hour)
        lblhour = findViewById(R.id.lbl_hour)
        relMinute = findViewById(R.id.rel_minute)
        lblMinute = findViewById(R.id.lbl_minute)
        relSecond = findViewById(R.id.rel_second)
        lblSecond = findViewById(R.id.lbl_second)
        linearDataUsage = findViewById(R.id.linear_data_usage)
        lblUsedSize = findViewById(R.id.lbl_used_size)
        lblLimitSize = findViewById(R.id.lbl_limit_size)
        linearUnlimited = findViewById(R.id.linear_unlimited)

        countryName = intent.getStringExtra("country_name")
    }

    private fun fillData() {
        if(countryName == "Best Country"){
            lblCountryFlag.text = "\uD83C\uDF0E"
            lblCountryName.text = "Best Country"
        }else {
            lblCountryFlag.text = CommonClass().getCountryFlag(countryName!!)
            val locale = Locale("",countryName!!)
            lblCountryName.text = locale.displayName
        }

        if(isUnlimited!!){
            linearDataUsage.visibility = View.GONE
            linearUnlimited.visibility = View.VISIBLE
        }else{
            linearDataUsage.visibility = View.VISIBLE
            linearUnlimited.visibility = View.GONE

            lblLimitSize.text =
                Formatter.formatShortFileSize(this@DisconnectDetailsActivity,trafficLimit!!).toUpperCase(
                    Locale.getDefault())
            lblUsedSize.text =
                (Formatter.formatShortFileSize(this@DisconnectDetailsActivity,trafficUsed!!)).toUpperCase(
                    Locale.getDefault())
        }

        val disconnectTime = SharedPrefHelper()
            .getLong(SharedPrefHelper.DISCONNECT_TIME)
        val connectTime = SharedPrefHelper()
            .getLong(SharedPrefHelper.CONNECT_TIME)

        val diff = disconnectTime - connectTime
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        lblDay.text = String.format("%02d",days)
        lblhour.text = String.format("%02d",hours % 24)
        lblMinute.text = String.format("%02d",minutes % 60)
        lblSecond.text = String.format("%02d",seconds % 60)
    }

}
