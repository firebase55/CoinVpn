package com.A_Developer.CoinVpn

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.A_Developer.CoinVpn.helpers.DBHelper
import com.A_Developer.CoinVpn.interfaces.OnCoinAddedListener

class AddCoinActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val coinAmount = intent.getIntExtra("coin_amount",0)

        DBHelper(this).addCoin(coinAmount)
        try{
            onCoinAddedListener!!.coinAdded(coinAmount)
        } catch (e : Exception) {}


        val alertDialogView = layoutInflater.inflate(R.layout.dialog_coin_added,null)
        alertDialogView.findViewById<TextView>(R.id.lbl_coins).apply {
            text = "+$coinAmount Coins"
        }

        val builder = AlertDialog.Builder(this@AddCoinActivity).apply {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setView(alertDialogView)
        }

        val alertDialog = builder.create().apply {
            window!!.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
        alertDialog.show()

        alertDialog.setOnDismissListener {
            finish()
        }
    }

    companion object {
        var onCoinAddedListener : OnCoinAddedListener? = null
    }
}
