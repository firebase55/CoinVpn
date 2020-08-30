package com.A_Developer.CoinVpn.ads

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.A_Developer.CoinVpn.R
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.AdSize
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class BannerAdView : RelativeLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        loadFacebookBannerAd()
    }

    private fun loadFacebookBannerAd(){
        val bannerAdView = FBBannerAd(
            context,
            context.getString(R.string.facebook_banner_id),
            AdSize.BANNER_HEIGHT_50
        )
        bannerAdView.loadAd()
        bannerAdView.setAdListener(object : com.facebook.ads.AdListener{
            override fun onAdClicked(p0: Ad?) {}

            override fun onError(p0: Ad?, p1: AdError?) {
                println(""+p1!!.errorCode+"     "+p1.errorMessage)
                showGoogleBannerAd()
            }

            override fun onAdLoaded(p0: Ad?) {
                this@BannerAdView.addView(bannerAdView)
            }

            override fun onLoggingImpression(p0: Ad?) { }
        })
    }

    private fun showGoogleBannerAd(){
        val googleBannerAdView = AdView(context).apply {
            adSize = GoogleAdSize.BANNER
            adUnitId = context.getString(R.string.google_banner_id)
            loadAd(AdRequest.Builder().build())
        }
        googleBannerAdView.adListener = object : AdListener(){
            override fun onAdLoaded() {
                this@BannerAdView.addView(googleBannerAdView)
            }

            override fun onAdFailedToLoad(p0: Int) {
                this@BannerAdView.visibility = View.GONE
                println("Google Ad Error $p0")
            }
        }
    }
}