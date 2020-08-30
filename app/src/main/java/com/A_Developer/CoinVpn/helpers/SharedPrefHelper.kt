package com.A_Developer.CoinVpn.helpers

import android.content.Context
import com.A_Developer.CoinVpn.app.VpnApp

class SharedPrefHelper {

    fun putLong(key : String, value : Long){
        val sharedPreferences = VpnApp.appContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor =  sharedPreferences!!.edit()
        editor.putLong(key,value)
        editor.apply()
    }

    fun putInt(key: String, value: Int){
        val sharedPreferences = VpnApp.appContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor =  sharedPreferences!!.edit()
        editor.putInt(key,value)
        editor.apply()
    }

    fun putString(key: String, value : String){
        val sharedPreferences = VpnApp.appContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor =  sharedPreferences!!.edit()
        editor.putString(key,value)
        editor.apply()
    }

    fun putBoolean(key : String, value : Boolean) {
        val sharedPreferences = VpnApp.appContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor =  sharedPreferences!!.edit()
        editor.putBoolean(key,value)
        editor.apply()
    }

    fun getLong(key : String): Long {
        val sharedPreferences = VpnApp.appContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getLong(key,0)
    }

    fun getInt(key: String) : Int {
        val sharedPreferences = VpnApp.appContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key,0)
    }

    fun getString(key: String): String? {
        val sharedPreferences = VpnApp.appContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key,"Best Country")
    }

    fun getBoolean(key: String): Boolean {
        val sharedPreferences = VpnApp.appContext!!.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key,false)
    }



    companion object{
        const val SHARED_PREF_NAME = "vpn_app"
        /*
        * this constant are used for get total time spend on VPN.
        */
        const val CONNECT_TIME = "connect_time"
        const val DISCONNECT_TIME = "disconnect_time"
        const val LASTCONNECTED_COUNTRY = "last_connected_country"

        /*
        * this constant are used for saving setting data
        */
        const val UNPROTECTED_WIFI = "unprotected_wifi"
        const val ALTERNATIVE_PROTOCOL = "alternative_protocol"
        const val NAT_FIREWALL = "nat_firewall"
        const val MALWARE_SPYWARE = "malware_spyware_security"
        const val NOTIFICATION_TOOLBAR = "notification_toolbar"

        const val FIRST_TIME = "first_time" //if it is first launch after install than notification toolbar show

        /*
        * for Check in dialog and getting coin
        */
        const val TODAY_LAUNCH = "today_launch"  //It contains today's date so if today's date and sharedpref date not match than show dailog at first lanch on every day for Seven day
        const val LAUNCH_DAY = "launch_day" // It contains which day is from install date in range of 1-7
        const val IS_TODAY_COIN_GET = "is_today_coin_get" // it contain today's check in complate and today's coin gatted
        const val TOTAL_DAYS = 7 // for how many days check in dialog to show
    }
}