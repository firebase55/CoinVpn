package com.A_Developer.CoinVpn.pojos

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.RemoteViews
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.A_Developer.CoinVpn.AddCoinActivity
import com.A_Developer.CoinVpn.R
import com.A_Developer.CoinVpn.helpers.SharedPrefHelper
import com.A_Developer.CoinVpn.receiver.*
import java.util.*

class CommonClass {
    
    fun getCountryFlag(countryCode : String): String {
        val locale = Locale("",countryCode)
        val flagOffset = 0x1F1E6
        val asciiOffset = 0x41


        val firstChar = Character.codePointAt(locale.country, 0) - asciiOffset + flagOffset
        val secondChar = Character.codePointAt(locale.country, 1) - asciiOffset + flagOffset

        return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))

    }

    fun showToolBoxNotification(context: Context){
        createNotificationChannel(context)

        val notificationLayout = RemoteViews(context.packageName, R.layout.notification_layout)

        val intentVPNServer = Intent(context,HandleVPNReceiver::class.java)
        val pIntentVPNServer = PendingIntent.getBroadcast(context,0,intentVPNServer,0)

        val intentNetOptimize = Intent(context,HandleNetOptimizeReceiver::class.java)
        val pIntentNetOptimize = PendingIntent.getBroadcast(context,0,intentNetOptimize,0)

        val intentNetProtector = Intent(context,HandleNetProtector::class.java)
        val pIntentNetProtector = PendingIntent.getBroadcast(context,0,intentNetProtector,0)

        val intentCheckIp = Intent(context,HandleCheckIpReceiver::class.java)
        val pIntentCheckIp = PendingIntent.getBroadcast(context,0,intentCheckIp,0)

        val intentClose = Intent(context,HandleCloseReceiver::class.java)
        val pIntentClose = PendingIntent.getBroadcast(context,0,intentClose,0)

        notificationLayout.setOnClickPendingIntent(R.id.linear_vpn_server,pIntentVPNServer)
        notificationLayout.setOnClickPendingIntent(R.id.linear_network_optimize,pIntentNetOptimize)
        notificationLayout.setOnClickPendingIntent(R.id.linear_cancel,pIntentNetProtector)
        notificationLayout.setOnClickPendingIntent(R.id.linear_check_ip,pIntentCheckIp)
        notificationLayout.setOnClickPendingIntent(R.id.linear_cancel,pIntentClose)

        val customNotification = NotificationCompat.Builder(context,WIDGET_NOTIFICATION_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.cancel)
            setCustomContentView(notificationLayout)
            setOngoing(true)
            setWhen(0)
            setAutoCancel(false)
        }

        with(NotificationManagerCompat.from(context)) {
            notify(WIDGET_NOTIFICATION_CHANNEL_ID.toInt(), customNotification.build())
        }
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                WIDGET_NOTIFICATION_CHANNEL_ID,"Widget",
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "this is Vpn App Notification Channel"


            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    fun cancelNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(WIDGET_NOTIFICATION_CHANNEL_ID.toInt())
    }

    fun showDailyCheckInDialog(context: Context) {

        val checkInDialogView = (context as Activity).layoutInflater.inflate(R.layout.dialog_check_in,null)
        val checkInDialogBuilder = AlertDialog.Builder(context).apply {
            setView(checkInDialogView)
        }

        val checkInDialog = checkInDialogBuilder.create().apply {
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        val lblNumberOfDay = checkInDialogView.findViewById<TextView>(R.id.lbl_number_of_day)
        val btnGetCoin = checkInDialogView.findViewById<Button>(R.id.btn_get_coin)
        val lblYouHaveChecked = checkInDialogView.findViewById<TextView>(R.id.lbl_you_have_checked)

        val checkedDay = SharedPrefHelper().getInt(SharedPrefHelper.LAUNCH_DAY)
        lblNumberOfDay.text = "${checkedDay.minus(1)} Days"

        if (SharedPrefHelper().getBoolean(SharedPrefHelper.IS_TODAY_COIN_GET)){
            btnGetCoin.visibility = View.GONE
            lblYouHaveChecked.visibility = View.VISIBLE
        }else{
            btnGetCoin.visibility = View.VISIBLE
            lblYouHaveChecked.visibility = View.GONE
        }

        var coinToAdd = 0
        if(checkedDay in 1..6){
            coinToAdd = checkedDay * 100
        } else if (checkedDay == 7){
            coinToAdd = 1000
        }

        btnGetCoin.setOnClickListener {
            SharedPrefHelper().putBoolean(SharedPrefHelper.IS_TODAY_COIN_GET,true)
            SharedPrefHelper().putInt(SharedPrefHelper.LAUNCH_DAY,checkedDay+1)
            checkInDialog.dismiss()
            val intent = Intent(context,AddCoinActivity::class.java).apply {
                putExtra("coin_amount",coinToAdd)
            }
            context.startActivity(intent)
        }
       checkInDialog.show()
    }

    companion object {
        const val WIDGET_NOTIFICATION_CHANNEL_ID = "102"
    }
}