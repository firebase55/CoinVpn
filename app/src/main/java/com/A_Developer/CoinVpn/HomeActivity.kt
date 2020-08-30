package com.A_Developer.CoinVpn

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar!!.hide()
        homeActivity = this

        var fragmentId : Int? = null
        if(intent.hasExtra("fragment_id")){
            fragmentId = intent.extras!!.getInt("fragment_id")
        }

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)
        try{
            navView.menu.performIdentifierAction(fragmentId!!,1)
        }catch (e : Exception){
            println(e.message)
        }

    }

    companion object {
        var homeActivity : HomeActivity? = null
    }
}
