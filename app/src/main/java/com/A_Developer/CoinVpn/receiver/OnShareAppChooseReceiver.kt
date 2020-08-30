package com.A_Developer.CoinVpn.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.Intent.EXTRA_CHOSEN_COMPONENT
import com.A_Developer.CoinVpn.AddCoinActivity


class OnShareAppChooseReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, _intent: Intent?) {

        val selectedApp = _intent!!.extras!!.get(EXTRA_CHOSEN_COMPONENT).toString()
        if(selectedApp.contains("SendTextToClipboardActivity")){
            val intent = Intent(context,AddCoinActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                putExtra("coin_amount", _intent.getIntExtra("coin_amount",0))
            }
            context!!.startActivity(intent)
        }

    }

}