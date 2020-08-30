package com.A_Developer.CoinVpn.ads

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.A_Developer.CoinVpn.R
import com.facebook.ads.*
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.MediaView
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import java.util.*

class NativeAdView : CardView {
    lateinit var nativeAd: UnifiedNativeAd

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        loadFacebookNativeAd()
    }

    private fun loadFacebookNativeAd(){
        val nativeAd =
            FBNativeAd(context, context.getString(R.string.facebook_native_ad))
        nativeAd.setAdListener(object : NativeAdListener {
            override fun onAdClicked(p0: Ad?) {}

            override fun onMediaDownloaded(p0: Ad?) {}

            override fun onError(p0: Ad?, p1: AdError?) {
                showGoogleNativeAd()
            }

            override fun onAdLoaded(p0: Ad?) {
                nativeAd.unregisterView()
                this@NativeAdView.addView(inflateFBNativeAd(context,nativeAd))
            }

            override fun onLoggingImpression(p0: Ad?) {}
        })
        nativeAd.loadAd()
    }
    private fun inflateFBNativeAd(context: Context, nativeAd: NativeAd?): View? {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val nativeAdView = layoutInflater.inflate(R.layout.facebook_native_ad_layout,null)

        val nativeAdContainer = nativeAdView.findViewById<NativeAdLayout>(R.id.native_ad_container)
        val imgAdIcon = nativeAdView.findViewById<com.facebook.ads.MediaView>(R.id.img_ad_icon)
        val lblAdTitle = nativeAdView.findViewById<TextView>(R.id.lbl_ad_title).apply {
            text = nativeAd!!.advertiserName
        }

        nativeAdView.findViewById<TextView>(R.id.lbl_ad_sponsored).apply {
            text = "Sponsored"
        }

        val lblAdSocialContext = nativeAdView.findViewById<TextView>(R.id.lbl_ad_social_context).apply {
            text = nativeAd!!.adSocialContext
        }

        val lblAdBody = nativeAdView.findViewById<TextView>(R.id.lbl_ad_body).apply {
            text = nativeAd!!.adBodyText
        }

        val btnAdCallAction = nativeAdView.findViewById<Button>(R.id.btn_ad_call_action).apply {
            text = nativeAd!!.adCallToAction
            visibility = if (nativeAd.hasCallToAction()) View.VISIBLE else View.INVISIBLE
        }

        val mediaViewMain = nativeAdView.findViewById<com.facebook.ads.MediaView>(R.id.media_view_main)
        val linearAdChoiceContainer = nativeAdView.findViewById<LinearLayout>(R.id.linear_ad_choices_container)

        if(linearAdChoiceContainer != null){
            val adOptionsView = AdOptionsView(context,nativeAd,nativeAdContainer)
            linearAdChoiceContainer.removeAllViews()
            linearAdChoiceContainer.addView(adOptionsView,0)
        }

        mediaViewMain.setListener(fbMediaViewListner)

        val clickableViews = ArrayList<View>()
        clickableViews.add(imgAdIcon)
        clickableViews.add(mediaViewMain)
        clickableViews.add(btnAdCallAction)
        nativeAd!!.registerViewForInteraction(nativeAdView, mediaViewMain, imgAdIcon, clickableViews)

        NativeAdBase.NativeComponentTag.tagView(imgAdIcon, NativeAdBase.NativeComponentTag.AD_ICON)
        NativeAdBase.NativeComponentTag.tagView(lblAdTitle, NativeAdBase.NativeComponentTag.AD_TITLE)
        NativeAdBase.NativeComponentTag.tagView(lblAdBody, NativeAdBase.NativeComponentTag.AD_BODY)
        NativeAdBase.NativeComponentTag.tagView(lblAdSocialContext, NativeAdBase.NativeComponentTag.AD_SOCIAL_CONTEXT)
        NativeAdBase.NativeComponentTag.tagView(btnAdCallAction, NativeAdBase.NativeComponentTag.AD_CALL_TO_ACTION)

        return nativeAdView
    }

    private var fbMediaViewListner = object : MediaViewListener{
        override fun onEnterFullscreen(p0: com.facebook.ads.MediaView?) {}

        override fun onVolumeChange(p0: com.facebook.ads.MediaView?, p1: Float) {}

        override fun onComplete(p0: com.facebook.ads.MediaView?) {}

        override fun onFullscreenForeground(p0: com.facebook.ads.MediaView?) {}

        override fun onFullscreenBackground(p0: com.facebook.ads.MediaView?) {}

        override fun onPause(p0: com.facebook.ads.MediaView?) {}

        override fun onExitFullscreen(p0: com.facebook.ads.MediaView?) {}

        override fun onPlay(p0: com.facebook.ads.MediaView?) {}
    }
}

fun NativeAdView.showGoogleNativeAd(){
    val googleNativeAd = AdLoader.Builder(context,context.getString(R.string.google_native_id)).forUnifiedNativeAd {
        nativeAd = it
        val adView = (context as Activity).layoutInflater.inflate(R.layout.google_native_ad_layout,null) as UnifiedNativeAdView
        populateUnifiedNativeAdView(nativeAd, adView)
        this.removeAllViews()
        this.addView(adView)
    }
        .build()
    googleNativeAd.loadAd(AdRequest.Builder().build())
}

fun populateUnifiedNativeAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView) {
    val mediaView = adView.findViewById<MediaView>(R.id.ad_media)
    adView.mediaView = mediaView
    adView.headlineView = adView.findViewById(R.id.ad_headline)
    adView.bodyView = adView.findViewById(R.id.ad_body)
    adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
    adView.iconView = adView.findViewById(R.id.ad_app_icon)
    adView.advertiserView = adView.findViewById(R.id.ad_advertiser)

    (adView.headlineView as TextView).text = nativeAd.headline
    if (nativeAd.body == null) {
        adView.bodyView.visibility = View.INVISIBLE
    } else {
        adView.bodyView.visibility = View.VISIBLE
        (adView.bodyView as TextView).text = nativeAd.body
    }

    if (nativeAd.callToAction == null) {
        adView.callToActionView.visibility = View.INVISIBLE
    } else {
        adView.callToActionView.visibility = View.VISIBLE
        (adView.callToActionView as Button).text = nativeAd.callToAction
    }

    if (nativeAd.icon == null) {
        adView.iconView.visibility = View.GONE
    } else {
        (adView.iconView as ImageView).setImageDrawable(
            nativeAd.icon.drawable
        )
        adView.iconView.visibility = View.VISIBLE
    }

    if (nativeAd.advertiser == null) {
        adView.advertiserView.visibility = View.INVISIBLE
    } else {
        (adView.advertiserView as TextView).text = nativeAd.advertiser
        adView.advertiserView.visibility = View.VISIBLE
    }

    adView.setNativeAd(nativeAd)
}