package com.A_Developer.CoinVpn.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.A_Developer.CoinVpn.HomeActivity

class HandleNetProtector : BroadcastReceiver() {
    override fun onReceive(context: Context?, p1: Intent?) {

        context!!.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        val intent = Intent(context, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}