package com.A_Developer.CoinVpn

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class SpinActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var imgSpin : ImageView
    private lateinit var btnMakeSpin : Button

    private var degree = 0F
    private var oldDegree = 0F

    private val HALF_SECTOR = 360F/10F/2F
    private val sectors = arrayOf(10,20,30,40,50,60,100,300,500,0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spin)
        supportActionBar!!.hide()

        initControl()
    }

    override fun onClick(view: View?) {
        when (view) {
            btnMakeSpin -> {
                startSpin()
            }
        }
    }

    private fun initControl() {
        imgSpin = findViewById(R.id.img_spin)
        btnMakeSpin = findViewById(R.id.btn_make_spin)

        btnMakeSpin.setOnClickListener(this)
    }

    private fun startSpin() {
        oldDegree = degree % 360
        degree = Random.nextInt(3600) + 720F

        val rotate = RotateAnimation(oldDegree,degree,RotateAnimation.RELATIVE_TO_SELF,0.5F,RotateAnimation.RELATIVE_TO_SELF,0.5F).apply {
            duration = 3600
            fillAfter = true
            interpolator = DecelerateInterpolator()
            setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(p0: Animation?) {

                }

                override fun onAnimationEnd(p0: Animation?) {
                    showSpinPrize(360-(degree % 360))
                }

                override fun onAnimationStart(p0: Animation?) {

                }
            })
        }
        imgSpin.startAnimation(rotate)
    }

    private fun showSpinPrize(degree: Float) {
        var i = 0
        do{
            val start = HALF_SECTOR * (i * 2 + 1)
            val end = HALF_SECTOR * (i * 2 + 3)

            if(degree >= start && degree < end){
                if(sectors[i] == 0){
                    Toast.makeText(this,"You have one more chance to spin wheel",Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this,AddCoinActivity::class.java).apply {
                        putExtra("coin_amount",sectors[i])
                    }
                    startActivity(intent)
                    finish()
                }
            }
            i++
        }while (i < sectors.size)
    }
}
