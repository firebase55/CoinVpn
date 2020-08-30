package com.A_Developer.CoinVpn.ads

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.A_Developer.CoinVpn.LoadingActivity
import com.A_Developer.CoinVpn.R
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

typealias GoogleInterstitialAd = com.google.android.gms.ads.InterstitialAd
typealias GoogleAdSize = com.google.android.gms.ads.AdSize

typealias FBInterstitialAd = com.facebook.ads.InterstitialAd
typealias FBBannerAd = com.facebook.ads.AdView
typealias FBNativeAd = com.facebook.ads.NativeAd
typealias FBInterstitialAdListener = com.facebook.ads.InterstitialAdListener

class AdManger {

    fun showFBInterstitialAd(context: Context,intent: Intent?){
        context.startActivity(Intent(context,LoadingActivity::class.java))
        val interstitialAd = FBInterstitialAd(
            context,
            context.getString(R.string.facebook_interstitial_id)
        )
        interstitialAd.setAdListener(object : FBInterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad?) {}

            override fun onAdClicked(ad: Ad?) {}

            override fun onInterstitialDismissed(ad: Ad?) {
                if(intent != null){
                    context.startActivity(intent)
                }
            }

            override fun onError(ad: Ad?, p1: AdError?) {
                showGoogleInterstitialAd(context,intent)
            }

            override fun onAdLoaded(ad: Ad?) {
                LoadingActivity.loadingActivity!!.finish()
                interstitialAd.show()
            }

            override fun onLoggingImpression(ad: Ad?) {
            }

        })
        interstitialAd.loadAd()
    }

    private fun showGoogleInterstitialAd(context: Context,intent: Intent?) {
        val googleInterstitialAd = GoogleInterstitialAd(context).apply {
            adUnitId = context.getString(R.string.google_interstitial_id)
        }
        googleInterstitialAd.adListener = object : AdListener(){
            override fun onAdFailedToLoad(p0: Int) {
                LoadingActivity.loadingActivity!!.finish()
                if(intent != null){
                    context.startActivity(intent)
                }
            }

            override fun onAdClosed() {
                if(intent != null){
                    context.startActivity(intent)
                }
            }

            override fun onAdLoaded() {
                LoadingActivity.loadingActivity!!.finish()
                googleInterstitialAd.show()
            }
        }
        googleInterstitialAd.loadAd(AdRequest.Builder().build())
    }

    fun showGoogleRewardVideoAd(context: Context, intent: Intent,handler : (isVideoClosed : Boolean) -> Unit) {
        context.startActivity(Intent(context, LoadingActivity::class.java))
        var isRewardEarned = false
        val googleRewardedVideoAd = RewardedAd(context,context.getString(R.string.google_reward_video))

        val rewardedVideoCallback = object : RewardedAdCallback() {
            override fun onUserEarnedReward(p0: RewardItem) {
                isRewardEarned = true
            }

            override fun onRewardedAdFailedToShow(p0: Int) {
                handler.invoke(true)
            }

            override fun onRewardedAdClosed() {
                LoadingActivity.loadingActivity!!.finish()
                if(isRewardEarned){
                    context.startActivity(intent)
                }
                handler.invoke(true)
            }

            override fun onRewardedAdOpened() {
            }
        }

        val adLoadCallback = object : RewardedAdLoadCallback() {
            override fun onRewardedAdLoaded() {
                googleRewardedVideoAd.show(context as Activity,rewardedVideoCallback)
            }

            override fun onRewardedAdFailedToLoad(errorCode: Int) {
                LoadingActivity.loadingActivity!!.finish()
                Toast.makeText(context,"You have no video", Toast.LENGTH_SHORT).show()
                handler.invoke(true)
            }
        }

        googleRewardedVideoAd.loadAd(AdRequest.Builder().build(),adLoadCallback)
    }
}