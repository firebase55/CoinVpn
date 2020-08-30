package com.A_Developer.CoinVpn

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.RelativeLayout
import android.widget.Switch
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.A_Developer.CoinVpn.ads.AdManger
import com.A_Developer.CoinVpn.helpers.SharedPrefHelper
import com.A_Developer.CoinVpn.pojos.CommonClass

class SettingsActivity : AppCompatActivity(), CompoundButton.OnCheckedChangeListener,
    View.OnClickListener {

    private lateinit var relAboutUs : RelativeLayout
    private lateinit var swtUnprotectedWiFi : Switch
    private lateinit var swtAlternativeProtocol : Switch
    private lateinit var swtNATFirewall : Switch
    private lateinit var swtMalwareSpyware : Switch
    private lateinit var swtNotificationToolbox : Switch

    private val adManager = AdManger()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar!!.hide()

        initControls()
        fillData()
    }

    override fun onClick(view : View?) {
        when(view){
            relAboutUs -> {
                val intent = Intent(this,AboutUsActivity::class.java)
                adManager.showFBInterstitialAd(this,intent)
            }
        }
    }
    override fun onCheckedChanged(switchView: CompoundButton?, isChecked: Boolean) {
        when (switchView) {
            swtUnprotectedWiFi -> {
                SharedPrefHelper()
                    .putBoolean(SharedPrefHelper.UNPROTECTED_WIFI,isChecked)
            }
            swtAlternativeProtocol -> {
                SharedPrefHelper()
                    .putBoolean(SharedPrefHelper.ALTERNATIVE_PROTOCOL,isChecked)
            }
            swtNATFirewall -> {
                SharedPrefHelper()
                    .putBoolean(SharedPrefHelper.NAT_FIREWALL,isChecked)
            }
            swtMalwareSpyware -> {
                SharedPrefHelper()
                    .putBoolean(SharedPrefHelper.MALWARE_SPYWARE,isChecked)
            }
            swtNotificationToolbox -> {
                if (isChecked){
                    SharedPrefHelper()
                        .putBoolean(SharedPrefHelper.NOTIFICATION_TOOLBAR,isChecked)
                    CommonClass().showToolBoxNotification(this)
                } else {
                    showRemoveToolboxDialog()
                }
            }
        }
    }
    private fun initControls() {
        relAboutUs = findViewById(R.id.rel_about_us)
        swtUnprotectedWiFi = findViewById(R.id.swt_unprotected_wifi)
        swtAlternativeProtocol = findViewById(R.id.swt_alternative_protocol)
        swtNATFirewall = findViewById(R.id.swt_nat_firewall)
        swtMalwareSpyware = findViewById(R.id.swt_malware_spyware)
        swtNotificationToolbox = findViewById(R.id.swt_notification_toolbox)

        relAboutUs.setOnClickListener(this)
        swtUnprotectedWiFi.setOnCheckedChangeListener(this)
        swtAlternativeProtocol.setOnCheckedChangeListener(this)
        swtNATFirewall.setOnCheckedChangeListener(this)
        swtMalwareSpyware.setOnCheckedChangeListener(this)
        swtNotificationToolbox.setOnCheckedChangeListener(this)
    }

    private fun fillData() {
        swtUnprotectedWiFi.isChecked = SharedPrefHelper()
            .getBoolean(SharedPrefHelper.UNPROTECTED_WIFI)
        swtAlternativeProtocol.isChecked = SharedPrefHelper()
            .getBoolean(SharedPrefHelper.ALTERNATIVE_PROTOCOL)
        swtNATFirewall.isChecked = SharedPrefHelper()
            .getBoolean(SharedPrefHelper.NAT_FIREWALL)
        swtMalwareSpyware.isChecked = SharedPrefHelper()
            .getBoolean(SharedPrefHelper.MALWARE_SPYWARE)
        swtNotificationToolbox.isChecked = SharedPrefHelper()
            .getBoolean(SharedPrefHelper.NOTIFICATION_TOOLBAR)
    }

    private fun showRemoveToolboxDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Turn off notification widget ?")
            setMessage("If turn off, you will unble to optimoze your phone quickle")
            setPositiveButton("Turn Off"){ dialog: DialogInterface, i: Int ->
                CommonClass().cancelNotification(context)
                SharedPrefHelper()
                    .putBoolean(SharedPrefHelper.NOTIFICATION_TOOLBAR,false)

            }
            setNegativeButton("Not Now"){ dialog: DialogInterface, i: Int ->
                dialog.cancel()
            }
            show()
        }
    }
}
