package com.A_Developer.CoinVpn.fragments


import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.A_Developer.CoinVpn.AddCoinActivity
import com.A_Developer.CoinVpn.R
import com.A_Developer.CoinVpn.SpinActivity
import com.A_Developer.CoinVpn.ads.AdManger
import com.A_Developer.CoinVpn.helpers.DBHelper
import com.A_Developer.CoinVpn.interfaces.OnCoinAddedListener
import com.A_Developer.CoinVpn.pojos.CommonClass
import com.A_Developer.CoinVpn.receiver.OnShareAppChooseReceiver


class CoinsFragment : Fragment(), View.OnClickListener, OnCoinAddedListener {

    private lateinit var coinsView : View

    private lateinit var lblCurrentCoin : TextView
    private lateinit var lblReceivedTodayCoins : TextView
    private lateinit var btnCheckIn : Button
    private lateinit var btnInviteFriend : Button
    private lateinit var btnWatchVideo : Button
    private lateinit var btnTapToCoin : Button
    private lateinit var btnSpin : Button

    private lateinit var dbHelper : DBHelper
    private var handler = Handler()
    private var seconds = 60
    private val adManager = AdManger()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        coinsView = inflater.inflate(R.layout.fragment_coins, container, false)

        initControls()
        fillData()


        handler.postDelayed(oneMinuteHandler, 1000)

        return coinsView
    }

    override fun onStop() {
        handler.removeCallbacks(oneMinuteHandler)
        super.onStop()
    }

    override fun onClick(view: View?) {
        when(view) {
            btnCheckIn -> {
                showCheckInDialog()
            }
            btnInviteFriend -> {
                shareApp()
            }
            btnWatchVideo -> {
                val intent = Intent(activity!!, AddCoinActivity::class.java).apply {
                    putExtra("coin_amount",150)
                }
                adManager.showGoogleRewardVideoAd(activity!!,intent){}
            }
            btnTapToCoin -> {
                if(seconds == 60){
                    val intent = Intent(activity!!, AddCoinActivity::class.java).apply {
                        putExtra("coin_amount",100)
                    }
                    adManager.showGoogleRewardVideoAd(activity!!,intent){
                        if(it){
                            handler.postDelayed(oneMinuteHandler,1000)
                        }
                    }
                }
            }
            btnSpin -> {
                val intent = Intent(activity!!,SpinActivity::class.java)
                adManager.showFBInterstitialAd(activity!!,intent)
            }
        }
    }

    override fun coinAdded(coinAmount: Int) {
        fillData()
    }

    private fun initControls() {
        lblCurrentCoin = coinsView.findViewById(R.id.lbl_current_coin)
        lblReceivedTodayCoins = coinsView.findViewById(R.id.lbl_recieved_today_coins)
        btnCheckIn = coinsView.findViewById(R.id.btn_check_in)
        btnInviteFriend = coinsView.findViewById(R.id.btn_invite_friend)
        btnWatchVideo = coinsView.findViewById(R.id.btn_watch_video)
        btnTapToCoin = coinsView.findViewById(R.id.btn_tap_to_coin)
        btnSpin = coinsView.findViewById(R.id.btn_spin)

        btnCheckIn.setOnClickListener(this)
        btnInviteFriend.setOnClickListener(this)
        btnWatchVideo.setOnClickListener(this)
        btnTapToCoin.setOnClickListener(this)
        btnSpin.setOnClickListener(this)

        AddCoinActivity.onCoinAddedListener = this

        dbHelper = DBHelper(activity)
    }

    private fun fillData(){
        val todayCoin = dbHelper.getTodayCoin()
        if(todayCoin.isEmpty() || todayCoin.toInt() < 0){
            lblReceivedTodayCoins.text = "0"
        } else {
            lblReceivedTodayCoins.text = todayCoin
        }
        lblCurrentCoin.text = dbHelper.getTotalCoin()

        (btnSpin.background as GradientDrawable).setColor(Color.parseColor("#F4C30A"))
        (btnCheckIn.background as GradientDrawable).setColor(Color.parseColor("#08E7C6"))
        (btnInviteFriend.background as GradientDrawable).setColor(Color.parseColor("#1572E3"))
        (btnWatchVideo.background as GradientDrawable).setColor(Color.parseColor("#F60158"))

    }

    private fun showCheckInDialog() {
        CommonClass().showDailyCheckInDialog(activity!!)
    }

    private fun shareApp() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody =
            "Hello Friends,\n\nInstall an amazing application from Play store to change your phone IP using VPN and earn coins and using below link https://play.google.com/store/apps/details?id=${activity!!.packageName}\n\nInstall this app to Download tiktok videos or sound and share to your friend and family member."
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)

        val receiverIntent = Intent(activity!!, OnShareAppChooseReceiver::class.java)
        receiverIntent.putExtra("coin_amount",80)
        val receiverPendingIntent = PendingIntent.getBroadcast(activity,0,receiverIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)
        startActivity(Intent.createChooser(sharingIntent, "Share Via",receiverPendingIntent.intentSender))

    }

    private var oneMinuteHandler = object : Runnable{
        override fun run() {
            btnTapToCoin.text = "$seconds S"
            seconds -= 1
            if (seconds > 0){
                handler.postDelayed(this,1000)
            } else {
                seconds = 60
                btnTapToCoin.text = "+100 Coins"
                handler.removeCallbacks(this)
            }
        }

    }

}
