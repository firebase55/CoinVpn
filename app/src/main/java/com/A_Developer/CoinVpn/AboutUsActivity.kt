package com.A_Developer.CoinVpn

import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.A_Developer.CoinVpn.ads.AdManger

class AboutUsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var lblAppVersion : TextView
    private lateinit var lblAlwaysOnVPN : TextView
    private lateinit var lblGiveUsFiveStar : TextView
    private lateinit var lblHelpUs : TextView
    private lateinit var lblShareApp : TextView

    private val adManager = AdManger()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)
        supportActionBar!!.hide()

        initControls()
    }

    override fun onClick(view: View?) {
        when(view){
            lblAlwaysOnVPN -> {
                showAlwaysOnDialog()
            }

            lblGiveUsFiveStar -> {
                openRateUs()
            }

            lblHelpUs -> {
                val intent = Intent(this,HelpUsActivity::class.java)
                adManager.showFBInterstitialAd(this,intent)
            }

            lblShareApp -> {
                shareApp()
            }
        }
    }

    private fun initControls(){
        lblAppVersion = findViewById(R.id.lbl_app_version)
        lblAlwaysOnVPN = findViewById(R.id.lbl_alwayson_vpn)
        lblGiveUsFiveStar = findViewById(R.id.lbl_give_us_five_star)
        lblHelpUs = findViewById(R.id.lbl_help_us)
        lblShareApp = findViewById(R.id.lbl_share_app)

        lblAlwaysOnVPN.setOnClickListener(this)
        lblGiveUsFiveStar.setOnClickListener(this)
        lblHelpUs.setOnClickListener(this)
        lblShareApp.setOnClickListener(this)

        lblAppVersion.text = BuildConfig.VERSION_NAME
    }

    private fun showAlwaysOnDialog(){
        AlertDialog.Builder(this).apply {
            setTitle("Always-On VPN")
            setMessage("Now you can make VPN connection persistent even after reboot.\nTo-Do so open system VPN Settings, tap the gear icon for VPNApplication and turn on Always-On VPN.")
            setPositiveButton("VPN Settings") { dialog: DialogInterface, _: Int ->
                openVPNSetting()
                dialog.dismiss()
            }
            setNegativeButton("Close") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            setCancelable(false)
            show()
        }
    }

    private fun openVPNSetting() {
        startActivity(Intent(Settings.ACTION_VPN_SETTINGS))
    }

    private fun openRateUs() {
        val uri = Uri.parse("http://play.google.com/store/apps/details?id=${packageName}")
        val gotoPlayStore = Intent(Intent.ACTION_VIEW, uri)
        gotoPlayStore.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        try {
            startActivity(gotoPlayStore)
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=${packageName}")))
        }
    }

    private fun shareApp() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody =
            "Hello Friends,\n\nInstall an amazing application from Play store to change your phone IP using VPN and earn coins and using below link https://play.google.com/store/apps/details?id=${packageName}\n\nInstall this app to unblock site access any content over world."
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share Via"))
    }
}
