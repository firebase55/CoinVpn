package com.A_Developer.CoinVpn.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.preference.PreferenceManager
import com.anchorfree.hydrasdk.HydraSDKConfig
import com.anchorfree.hydrasdk.HydraSdk
import com.anchorfree.hydrasdk.api.AuthMethod
import com.anchorfree.hydrasdk.api.ClientInfo
import com.anchorfree.hydrasdk.api.response.User
import com.anchorfree.hydrasdk.callbacks.Callback
import com.anchorfree.hydrasdk.exceptions.HydraException
import com.anchorfree.hydrasdk.vpnservice.connectivity.NotificationConfig
import com.A_Developer.CoinVpn.R
import com.A_Developer.CoinVpn.helpers.SharedPrefHelper
import com.google.android.gms.ads.MobileAds
import com.onesignal.OneSignal

class VpnApp : Application(){

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        initHydraSdk()
        loginToHydraSdk()
        initOnesignalSdk()
        putInitialValueForSettings()

        try{
            MobileAds.initialize(this)
        }catch (e : Exception){}


    }


    private fun initHydraSdk() {
        createNotificationChannel()
        val clientInfo = ClientInfo.newBuilder().apply {
            baseUrl("https://backend.northghost.com")
            carrierId("afdemo")
        }.build()

        val notificationConfig = NotificationConfig.newBuilder().apply {
            title(getString(R.string.app_name))
            channelId(NOTIFICATION_CHANNEL_ID)
        }.build()

        HydraSdk.setLoggingLevel(Log.VERBOSE)

        val config = HydraSDKConfig.newBuilder().apply {
            observeNetworkChanges(true)
            captivePortal(true)
            moveToIdleOnPause(false)
        }.build()

        HydraSdk.init(this,clientInfo,notificationConfig,config)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID,"VpnAppChannel",NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "this is Vpn App Notification Channel"

            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun loginToHydraSdk() {
        if(!HydraSdk.isLoggedIn()){
            val authMethod = AuthMethod.anonymous()
            HydraSdk.login(authMethod, object : Callback<User> {
                override fun success(user: User) {
                }

                override fun failure(e: HydraException) {
                    e.printStackTrace()
                }
            })
        }
    }

    private fun initOnesignalSdk() {
        OneSignal.startInit(this)
            .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
            .unsubscribeWhenNotificationsAreDisabled(true)
            .init()
    }

    private fun putInitialValueForSettings() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        if (!prefs.getBoolean("firstTime", false)) {

            val editor = prefs.edit()
            editor.putBoolean("firstTime", true)
            editor.apply()

            SharedPrefHelper()
                .putBoolean(SharedPrefHelper.FIRST_TIME,true)
            SharedPrefHelper()
                .putBoolean(SharedPrefHelper.UNPROTECTED_WIFI,true)
            SharedPrefHelper()
                .putBoolean(SharedPrefHelper.ALTERNATIVE_PROTOCOL,true)
            SharedPrefHelper()
                .putBoolean(SharedPrefHelper.NAT_FIREWALL,true)
            SharedPrefHelper()
                .putBoolean(SharedPrefHelper.MALWARE_SPYWARE,true)
            SharedPrefHelper()
                .putBoolean(SharedPrefHelper.NOTIFICATION_TOOLBAR,false)
            SharedPrefHelper()
                .putInt(SharedPrefHelper.LAUNCH_DAY,1)
        }
    }

    companion object{
        const val NOTIFICATION_CHANNEL_ID = "101"
        lateinit var mInstance: VpnApp
        val appContext: Context?
            get() {
                return mInstance.applicationContext
            }
    }

}