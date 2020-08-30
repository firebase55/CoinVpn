package com.A_Developer.CoinVpn

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.A_Developer.CoinVpn.pojos.CommonClass
import com.A_Developer.CoinVpn.helpers.SharedPrefHelper

class ShowDialogAcivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AlertDialog.Builder(this@ShowDialogAcivity).apply {
            setTitle("Turn off notification widget ?")
            setMessage("If turn off, you will unable to optimize your phone quickly.")
            setPositiveButton("Turn Off") { dialog: DialogInterface, i: Int ->
                CommonClass().cancelNotification(this@ShowDialogAcivity)
                SharedPrefHelper()
                    .putBoolean(SharedPrefHelper.NOTIFICATION_TOOLBAR, false)
                dialog.cancel()
                finish()

            }
            setNegativeButton("Not Now") { dialog: DialogInterface, i: Int ->
                dialog.cancel()
                finish()
            }
            show()
        }

    }
}
