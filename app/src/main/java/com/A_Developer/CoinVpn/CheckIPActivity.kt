package com.A_Developer.CoinVpn

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.A_Developer.CoinVpn.objects.ApiCaller
import com.A_Developer.CoinVpn.pojos.CommonClass
import com.A_Developer.CoinVpn.pojos.Geo
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.ResponseBody

class CheckIPActivity : AppCompatActivity(), View.OnClickListener {

    private var geo : Geo? = null

    private lateinit var relLoadingIp : RelativeLayout
    private lateinit var lblIp : TextView
    private lateinit var imgCopyIp : ImageView
    private lateinit var btnViewInMap : Button
    private lateinit var lblCountryFlag : TextView
    private lateinit var lblCountryName : TextView
    private lateinit var lblCityName : TextView
    private lateinit var lblRegionName : TextView
    private lateinit var lblContinentName : TextView
    private lateinit var lblIsp : TextView
    private lateinit var lblLatitude : TextView
    private lateinit var lblLongitude : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_ip)
        supportActionBar!!.hide()

        initControls()
        getIp()
    }

    override fun onClick(view: View?) {
        when(view){
            btnViewInMap -> {
                openLocationInMap()
            }

            imgCopyIp -> {
                copyIp()
            }
        }

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.ip_view_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_refresh -> {
                getIp()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initControls() {
        relLoadingIp = findViewById(R.id.rel_loading_ip)
        lblIp = findViewById(R.id.lbl_ip)
        imgCopyIp = findViewById(R.id.img_copy_ip)
        btnViewInMap = findViewById(R.id.btn_view_in_map)
        lblCountryFlag = findViewById(R.id.lbl_country_flag)
        lblCountryName = findViewById(R.id.lbl_country_name)
        lblCityName = findViewById(R.id.lbl_city_name)
        lblRegionName = findViewById(R.id.lbl_region_name)
        lblContinentName = findViewById(R.id.lbl_country_name)
        lblIsp = findViewById(R.id.lbl_isp)
        lblLatitude = findViewById(R.id.lbl_latitude)
        lblLongitude = findViewById(R.id.lbl_longitude)

        btnViewInMap.setOnClickListener(this)
        imgCopyIp.setOnClickListener(this)
    }

    private fun getIp(){
        ApiCaller.postRequest("json"){ responseBody: ResponseBody?, error: String? ->
            if(error != null){
                relLoadingIp.visibility = View.GONE
                Toast.makeText(this,"Could not get Ip", Toast.LENGTH_SHORT).show()
                return@postRequest
            }
            geo = ObjectMapper().readValue(responseBody!!.string(),Geo::class.java)
            if(geo != null || geo!!.status == "success"){
                relLoadingIp.visibility = View.GONE
                fillData()
            } else {
                relLoadingIp.visibility = View.GONE
                Toast.makeText(this,"Could not get Ip", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun fillData() {
        lblIp.text = geo!!.ip
        lblCountryFlag.text = CommonClass().getCountryFlag(geo!!.countryCode!!)
        lblCountryName.text = geo!!.country
        lblCityName.text = geo!!.city
        lblRegionName.text = geo!!.regionName
        lblContinentName.text = geo!!.timezone!!.substringBeforeLast("/")
        lblIsp.text = geo!!.isp
        lblLatitude.text = geo!!.lat
        lblLongitude.text = geo!!.lon
    }

    private fun openLocationInMap() {
        if(geo?.lat != null && geo?.lon != null){
            val gmmIntentUri = Uri.parse("geo:${geo!!.lat},${geo!!.lon}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
                setPackage("com.google.android.apps.maps")
            }
            startActivity(mapIntent)
        }
    }

    private fun copyIp() {
        if(geo?.ip != null){
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("simple text",geo!!.ip)
            clipboard.setPrimaryClip(clip)
        }

    }
}
