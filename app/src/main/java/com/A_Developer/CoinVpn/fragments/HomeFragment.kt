package com.A_Developer.CoinVpn.fragments

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.A_Developer.CoinVpn.*
import com.A_Developer.CoinVpn.ads.AdManger
import com.A_Developer.CoinVpn.helpers.DBHelper
import com.A_Developer.CoinVpn.helpers.HydraSdkHelper
import com.A_Developer.CoinVpn.helpers.SharedPrefHelper
import com.A_Developer.CoinVpn.interfaces.OnCountryChooseListner
import com.A_Developer.CoinVpn.pojos.CommonClass
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(), View.OnClickListener, OnCountryChooseListner {

    private lateinit var homeFragment : View

    private lateinit var relCountryNameContainer : RelativeLayout
    private lateinit var lblCountryFlag : TextView
    private lateinit var lblCountryName : TextView

    private lateinit var imgConnect : ImageView
    private lateinit var lblConnectionStatus : TextView

    private lateinit var lblNoteFirst : TextView
    private lateinit var imgThreat1 : ImageView
    private lateinit var lblNoteSecond : TextView
    private lateinit var imgThreat2 : ImageView

    private lateinit var btnGetReward : Button

    private lateinit var btnCheckIp : Button
    private lateinit var btnSetting : Button
    private lateinit var btnNetworkProtector : Button
    private lateinit var btnNetworkOptimize : Button
    private lateinit var btnRateUs : Button
    private lateinit var btnShareApp : Button

    private var countryName = "Best Country"
    private var serverPrice = 0
    private var dbHelper : DBHelper? = null
    private var adManager = AdManger()
    private var hydraSdkHelper = HydraSdkHelper()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        homeFragment = inflater.inflate(R.layout.fragment_home, container, false)

        initControls()
        fillData()
        showNotificationToolbox()
        showDailyCheckInDialog()

        return homeFragment
    }

    override fun onClick(view: View?) {
        when(view){
            relCountryNameContainer -> {
                startCountryChooseActivity()
            }

            imgConnect -> {
                connectOrDisconnectVPN()
            }
            btnCheckIp -> {
                val intent = Intent(activity!!,CheckIPActivity::class.java)
                adManager.showFBInterstitialAd(activity!!,intent)
            }
            btnSetting -> {
                val intent = Intent(activity!!,SettingsActivity::class.java)
                adManager.showFBInterstitialAd(activity!!,intent)
            }
            btnNetworkProtector -> {
                val intent = Intent(activity!!,NetworkProtectorActivity::class.java)
                adManager.showFBInterstitialAd(activity!!,intent)
            }

            btnNetworkOptimize -> {
                val intent = Intent(activity!!,NetworkOptimizeActivity::class.java)
                adManager.showFBInterstitialAd(activity!!,intent)
            }
            btnGetReward -> {
                val intent = Intent(activity!!, AddCoinActivity::class.java).apply {
                    putExtra("coin_amount",600)
                }
                adManager.showGoogleRewardVideoAd(activity!!,intent){}
            }
            btnRateUs -> {
                openRateUs()
            }
            btnShareApp -> {
                shareApp()
            }
        }
    }

    override fun onCountryChoose(countryName: String,serverPrice : Int) {
        if(countryName == "Best Country"){
            this.countryName = ""
            lblCountryName.text = countryName
            lblCountryFlag.text = "\uD83C\uDF0E"
            this.serverPrice = serverPrice
        } else {
            this.countryName = countryName
            val locale = Locale("",countryName)
            lblCountryName.text = locale.displayCountry
            lblCountryFlag.visibility = View.VISIBLE
            lblCountryFlag.text = CommonClass().getCountryFlag(countryName)
            this.serverPrice = serverPrice
        }

        if(hydraSdkHelper.isConnected()){
            hydraSdkHelper.disconnectFromVPNSimple {
                if(it){
                    connectToVPN()
                } else {
                    this@HomeFragment.countryName = "Best Country"
                    connectToVPNToOptimalServer()
                }
            }
        }
    }

    private fun initControls() {
        relCountryNameContainer = homeFragment.findViewById(R.id.rel_country_name_container)
        lblCountryFlag = homeFragment.findViewById(R.id.lbl_country_flag)
        lblCountryName = homeFragment.findViewById(R.id.lbl_country_name)
        imgConnect = homeFragment.findViewById(R.id.img_connect)
        lblConnectionStatus = homeFragment.findViewById(R.id.lbl_connection_status)
        lblNoteFirst = homeFragment.findViewById(R.id.lbl_note_first)
        lblNoteSecond = homeFragment.findViewById(R.id.lbl_note_second)
        imgThreat1 = homeFragment.findViewById(R.id.img_threat)
        imgThreat2 = homeFragment.findViewById(R.id.img_threat1)
        btnCheckIp = homeFragment.findViewById(R.id.btn_check_ip)
        btnSetting = homeFragment.findViewById(R.id.btn_setting)
        btnNetworkProtector = homeFragment.findViewById(R.id.btn_network_protector)
        btnNetworkOptimize = homeFragment.findViewById(R.id.btn_network_optimize)
        btnGetReward = homeFragment.findViewById(R.id.btn_get_reward)
        btnRateUs = homeFragment.findViewById(R.id.btn_rate_us)
        btnShareApp = homeFragment.findViewById(R.id.btn_share_app)

        relCountryNameContainer.setOnClickListener(this)
        imgConnect.setOnClickListener(this)
        btnCheckIp.setOnClickListener(this)
        btnSetting.setOnClickListener(this)
        btnNetworkProtector.setOnClickListener(this)
        btnNetworkOptimize.setOnClickListener(this)
        btnGetReward.setOnClickListener(this)
        btnRateUs.setOnClickListener(this)
        btnShareApp.setOnClickListener(this)

        SuperFastServerFragment.onCountryChooseListner = this
        ExtraSuperFastServerFragment.onCountryChooseListener = this
        dbHelper = DBHelper(activity!!)
    }

    private fun fillData() {
        countryName = SharedPrefHelper().getString(SharedPrefHelper.LASTCONNECTED_COUNTRY)!!
        if(countryName == "Best Country"){
            lblCountryFlag.text = "\uD83C\uDF0E"
            lblCountryName.text = countryName
        } else {
            lblCountryFlag.text = CommonClass().getCountryFlag(countryName)
            val locale = Locale("",countryName)
            lblCountryName.text = locale.displayName
        }
        if(hydraSdkHelper.isConnected() or hydraSdkHelper.isConnecting()){
            changeUIForStart()
        } else {
            changeUIForStop()
        }
    }

    private fun showNotificationToolbox() {
        if (SharedPrefHelper().getBoolean(SharedPrefHelper.NOTIFICATION_TOOLBAR)){
            CommonClass().showToolBoxNotification(activity!!)
        }
        if(SharedPrefHelper().getBoolean(SharedPrefHelper.FIRST_TIME)){
            SharedPrefHelper()
                .putBoolean(SharedPrefHelper.FIRST_TIME,false)
            CommonClass().showToolBoxNotification(activity!!)
        }
    }

    private fun showDailyCheckInDialog() {
        val sharedPrefDate = SharedPrefHelper()
            .getString(SharedPrefHelper.TODAY_LAUNCH)
        val today = SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(Date(System.currentTimeMillis()))
        val numberOfLaunch = SharedPrefHelper()
            .getInt(SharedPrefHelper.LAUNCH_DAY)

        if(sharedPrefDate != today){
            SharedPrefHelper().putString(SharedPrefHelper.TODAY_LAUNCH,today)
            SharedPrefHelper().putBoolean(SharedPrefHelper.IS_TODAY_COIN_GET,false)
            if(numberOfLaunch <= SharedPrefHelper.TOTAL_DAYS){
                CommonClass().showDailyCheckInDialog(activity!!)
            }
        }
    }

    private fun startCountryChooseActivity() {
        val intent = Intent(activity!!,ServerCountryActivity::class.java)
        adManager.showFBInterstitialAd(activity!!,intent)
    }

    private fun connectOrDisconnectVPN() {
        if(hydraSdkHelper.isConnected()){
            showDisconnectDialog()
        } else {

            if(countryName == "Best Country"){
                connectToVPNToOptimalServer()
            }else{
                connectToVPN()
            }
        }
    }

    private fun showDisconnectDialog() {
        AlertDialog.Builder(activity!!).apply {
            setMessage("Are you sure to want to disconnect VPN?")
            setPositiveButton("Disconnect") { dialog: DialogInterface, _: Int ->
                disconnectFromVPN()
                dialog.dismiss()
            }
            setNegativeButton("Keep Connected") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            setCancelable(true)
            show()
        }
    }

    private fun connectToVPNToOptimalServer() {
        lblConnectionStatus.text = "Connecting"
        hydraSdkHelper.connectVPNToOptimalLocation(){
            if(it){
                changeUIForStart()
                adManager.showFBInterstitialAd(activity!!,null)
            } else {
                changeUIForStop()
            }
        }
    }

    private fun connectToVPN() {
        if(serverPrice <= dbHelper!!.getTotalCoin().toInt()){
            lblConnectionStatus.text = "Connecting"
            hydraSdkHelper.connectToVPN(countryName,serverPrice){
                if(it){
                    changeUIForStart()
                    adManager.showFBInterstitialAd(activity!!,null)
                } else {
                    changeUIForStop()
                }
            }
        } else {
            showNotEnoughCoinDialog()
        }
    }

    private fun disconnectFromVPN() {
        lblConnectionStatus.text = "Disconnecting"
        hydraSdkHelper.disconnectFromVPN(){
            if(it){
                changeUIForStop()
                val intent = Intent(activity!!, DisconnectDetailsActivity::class.java)
                intent.putExtra("country_name",countryName)
                adManager.showFBInterstitialAd(activity!!,intent)
            } else {
                changeUIForStart()
            }
        }
    }

    private fun changeUIForStart() {
        imgConnect.setImageResource(R.drawable.connected)
        lblConnectionStatus.text = "Tap to disconnect"
        lblNoteFirst.text = "You have got a secure public Ip now."
        lblNoteSecond.text = "Your network data has been encrypted by bank level algorithms."
        imgThreat1.setImageResource(R.drawable.open)
        imgThreat2.setImageResource(R.drawable.open)
    }

    private fun changeUIForStop() {
        imgConnect.setImageResource(R.drawable.connect)
        lblConnectionStatus.text = "Tap to connect"
        lblNoteFirst.text = "Your Ip & Network are exposed to an unsafe server."
        lblNoteSecond.text = "Your Data is not encrypted and unsafe."
        imgThreat1.setImageResource(R.drawable.close)
        imgThreat2.setImageResource(R.drawable.close)
    }


    private fun showNotEnoughCoinDialog() {
        AlertDialog.Builder(activity!!).apply {
            setTitle("Oops you have not coin!!")
            setMessage("You have not enough coin to connect to that server. But you can earn coin from the app or you can open gift also and try to connect with top server speed.")
            setPositiveButton("Take me their"){ dialog: DialogInterface, _: Int ->
                val intent = Intent(activity!!,HomeActivity::class.java).apply {
                    putExtra("fragment_id",R.id.nav_coin)
                }
                startActivity(intent)
                dialog.dismiss()
            }
            setNegativeButton("Cancel"){ dialog: DialogInterface, _: Int ->
                changeUIForStop()
                dialog.dismiss()
            }
            setCancelable(false)
            show()
        }
    }

    private fun openRateUs(){
        val uri = Uri.parse("http://play.google.com/store/apps/details?id=${activity!!.packageName}")
        val gotoPlayStore = Intent(Intent.ACTION_VIEW, uri)
        gotoPlayStore.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(gotoPlayStore)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=${activity!!.packageName}")))
        }
    }
    private fun shareApp(){
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody =
            "Hello Friends,\n\nInstall an amazing application from Play store to change your phone IP using VPN and earn coins and using below link https://play.google.com/store/apps/details?id=${activity!!.packageName}\n\nInstall this app to unblock site access any content over world."
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share Via"))
    }
}