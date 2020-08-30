package com.A_Developer.CoinVpn.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.A_Developer.CoinVpn.ServerCountryActivity

class HandleVPNReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, p0: Intent?) {
        context!!.sendBroadcast(Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS))
        val intent = Intent(context,ServerCountryActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)

    }
}