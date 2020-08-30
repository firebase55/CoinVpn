package com.A_Developer.CoinVpn

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.ads.AudienceNetworkAds

class SplashActivity : AppCompatActivity() {

    private lateinit var imgAppIcon : ImageView
    private lateinit var lblAppName : TextView
    private lateinit var lblSplashSubtitle : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()

        AudienceNetworkAds.initialize(this)

        openLauncherActivity()

        initControls()

        val animSlideUp = AnimationUtils.loadAnimation(this,R.anim.slide_up)
        animSlideUp.setAnimationListener(lblSplashSubtitleAnimationListener)
        lblSplashSubtitle.startAnimation(animSlideUp)

        val animFadeUp = AnimationUtils.loadAnimation(this,R.anim.fade_up)
        animFadeUp.setAnimationListener(imgAppIconAnimationListener)
        imgAppIcon.startAnimation(animFadeUp)

    }

    private fun openLauncherActivity(){
        val defaultPref = getSharedPreferences(packageName, Context.MODE_PRIVATE)
        if(defaultPref.getBoolean("firstRun",true)){
            defaultPref.edit().putBoolean("firstRun",false).apply()
            val intent = Intent(this,OneTimeSplashActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initControls() {
        imgAppIcon = findViewById(R.id.img_app_icon)
        lblAppName = findViewById(R.id.lbl_app_name)
        lblSplashSubtitle = findViewById(R.id.lbl_splash_subtitle)
    }

    private var lblSplashSubtitleAnimationListener = object : Animation.AnimationListener{
        override fun onAnimationRepeat(animation : Animation?) {

        }

        override fun onAnimationEnd(animation : Animation?) {
            startActivity(Intent(this@SplashActivity,HomeActivity::class.java))
            finish()
        }

        override fun onAnimationStart(animation : Animation?) {

        }
    }

    private var imgAppIconAnimationListener = object : Animation.AnimationListener{
        override fun onAnimationRepeat(animation : Animation?) {

        }

        override fun onAnimationEnd(animation : Animation?) {
        }

        override fun onAnimationStart(animation : Animation?) {

        }
    }
}
