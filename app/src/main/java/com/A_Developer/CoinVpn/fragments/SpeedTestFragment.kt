package com.A_Developer.CoinVpn.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.A_Developer.CoinVpn.R
import com.A_Developer.CoinVpn.handlers.DownloadTest
import com.A_Developer.CoinVpn.handlers.GetNearestServer
import com.A_Developer.CoinVpn.handlers.PingTest
import com.A_Developer.CoinVpn.handlers.UploadTest


class SpeedTestFragment : Fragment(), View.OnClickListener {

    private lateinit var speedTestView : View
    private lateinit var imgBar : ImageView

    private lateinit var lblStartTest : TextView
    private lateinit var lblTestInfo : TextView
    private lateinit var lblPing : TextView
    private lateinit var lblDownload : TextView
    private lateinit var lblUpload : TextView

    private lateinit var getNearestServer: GetNearestServer
    private lateinit var pingTest : PingTest
    private lateinit var downloadTest: DownloadTest
    private lateinit var uploadTest: UploadTest

    private lateinit var rotate: RotateAnimation

    private val handler = Handler()
    private var isSpeedTestOnGoing = false

    private var position = 0F
    private var lastPosition = 0F

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        speedTestView = inflater.inflate(R.layout.fragment_speedtest, container, false)

        initControls()
        return speedTestView
    }

    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(waitForGetNearestServer)
        handler.removeCallbacks(waitForCompletingPingTest)
        handler.removeCallbacks(waitForCompleteDownloadTest)
        handler.removeCallbacks(waitForCompleteUploadTest)
    }

    override fun onClick(view: View?) {
        when(view){
            lblStartTest -> {
                if(!isSpeedTestOnGoing){
                    fillStartData()
                    getServerAddress()
                }
            }
        }
    }


    private fun initControls() {
        imgBar = speedTestView.findViewById(R.id.img_bar)
        lblStartTest = speedTestView.findViewById(R.id.lbl_start_test)
        lblTestInfo = speedTestView.findViewById(R.id.lbl_test_info)
        lblPing = speedTestView.findViewById(R.id.lbl_ping)
        lblDownload = speedTestView.findViewById(R.id.lbl_download)
        lblUpload = speedTestView.findViewById(R.id.lbl_upload)

        lblStartTest.setOnClickListener(this)

        imgBar.rotation = 240F
    }

    private fun fillStartData() {
        isSpeedTestOnGoing = true
        lblStartTest.visibility = View.GONE
        lblTestInfo.text = "Getting best server near you."
        lblPing.text = "0.00"
        lblDownload.text = "00.00"
        lblUpload.text = "00.00"
    }

    private fun getServerAddress() {
        getNearestServer = GetNearestServer()
        getNearestServer.start()
        handler.postDelayed(waitForGetNearestServer,100)
    }

    private fun startPingTest() {
        pingTest = PingTest(getNearestServer.nearestServer.host!!.replace(":8080",""))
        pingTest.start()
        handler.postDelayed(waitForCompletingPingTest,100)
    }

    private fun startDownloadTest() {
        val unformattedUrl = getNearestServer.nearestServer.url!!
        val fileUrl = unformattedUrl.replace(unformattedUrl.split("/")[unformattedUrl.split("/").size - 1], "")

        downloadTest = DownloadTest(fileUrl)
        downloadTest.start()
        handler.postDelayed(waitForCompleteDownloadTest,100)
    }

    private fun startUploadTest() {
        uploadTest = UploadTest(getNearestServer.nearestServer.url!!)
        uploadTest.start()
        handler.postDelayed(waitForCompleteUploadTest,100)
    }

    private fun cancelSpeedTest() {
        try {
            getNearestServer.interrupt()
            pingTest.interrupt()
            downloadTest.interrupt()
            uploadTest.interrupt()
        }catch (e : Exception){
            println(e.message)
        }
    }

    private fun setMeterNeedle(speed: Double) {
        position = getPositionByRate(speed)
        rotate = RotateAnimation(lastPosition,position,Animation.RELATIVE_TO_SELF,0.5F,Animation.RELATIVE_TO_SELF,0.5F).apply {
            interpolator = LinearInterpolator()
            duration = 100
        }
        imgBar.startAnimation(rotate)
        lastPosition = position
    }

    private fun getPositionByRate(rate: Double) : Float {
        var angle = 0F
//        0 1 5 10 20 30 50 75 100

        if(rate <= 0){
            angle = 0F
        }
        if(rate > 0 && rate < 1){
            angle = 0 + (rate * 9).toFloat()
        }
        if(rate >= 1 && rate < 5) {
            angle = 32 + ((rate - 1) * 8).toFloat()
        }
        if(rate >= 5 && rate < 10){
            angle = 61 + ((rate - 5) * 6).toFloat()
        }
        if(rate >= 10 && rate < 20) {
            angle = 90 + ((rate - 10) * 3).toFloat()
        }
        if(rate >= 20 && rate < 30) {
            angle = 120 +((rate - 20) * 3.2).toFloat()
        }
        if(rate >= 30 && rate < 50) {
            angle = 152 + ((rate - 30) * 1.4).toFloat()
        }
        if(rate >= 50 && rate < 75) {
            angle = 180 + ((rate - 50) * 1.1).toFloat()
        }
        if(rate >= 75 && rate < 100){
            angle = 209 + ((rate - 75) * 1.2).toFloat()
        }
        if(rate >= 100){
            angle = 239F
        }

        return angle
    }


    private var waitForGetNearestServer = object : Runnable{
        override fun run() {
            if(!getNearestServer.isFinished){
                handler.postDelayed(this,100)
            } else {
                lblTestInfo.text = "You were connected to ${getNearestServer.nearestServer.host}"
                startPingTest()
            }
        }
    }

    private var waitForCompletingPingTest = object : Runnable {
        override fun run() {
            if(!pingTest.isPingTestFinished){
                lblPing.text = pingTest.pingRate.toString()
                handler.postDelayed(this,100)
            } else {
                startDownloadTest()
            }
        }
    }

    private var waitForCompleteDownloadTest = object : Runnable {
        override fun run() {
            if(!downloadTest.isDownloadTestFinish){
                lblDownload.text = downloadTest.downloadSpeed.toString()
                setMeterNeedle(downloadTest.downloadSpeed)
                handler.postDelayed(this,100)
            } else {
                startUploadTest()
            }
        }
    }

    private var waitForCompleteUploadTest = object : Runnable {
        override fun run() {
            if(!uploadTest.isUploadComplete){
                lblUpload.text = uploadTest.uploadSpeed.toString()
                setMeterNeedle(uploadTest.uploadSpeed)
                handler.postDelayed(this,100)
            } else {
                lblTestInfo.text = "Speed Test Completed.. try one more time"
                lblStartTest.visibility = View.VISIBLE
                lblStartTest.text = "test"
                isSpeedTestOnGoing = false
                cancelSpeedTest()
            }
        }
    }
}