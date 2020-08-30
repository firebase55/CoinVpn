package com.A_Developer.CoinVpn

import android.content.Intent
import android.os.Bundle
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.A_Developer.CoinVpn.adapters.IntroPagerAdapter
import com.rd.PageIndicatorView

class OneTimeSplashActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewpagerIntro : ViewPager
    private lateinit var pageIndicatorView : PageIndicatorView
    private lateinit var btnAcceptAndContinue : Button
    private lateinit var lblBottomLine : TextView

    private lateinit var introPagerAdapter: IntroPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one_time_splash)
        supportActionBar!!.hide()

        initControls()
        fillData()
    }

    override fun onBackPressed() {

    }

    override fun onClick(view: View?) {
        when(view){
            btnAcceptAndContinue -> {
                if(viewpagerIntro.currentItem == 3){
                    startActivity(Intent(this@OneTimeSplashActivity,HomeActivity::class.java))
                    finish()
                } else {
                    viewpagerIntro.setCurrentItem(viewpagerIntro.currentItem+1,true)
                }
            }
        }
    }

    private fun initControls() {
        viewpagerIntro = findViewById(R.id.viewpager_intro)
        pageIndicatorView = findViewById(R.id.pageIndicatorView)
        btnAcceptAndContinue = findViewById(R.id.btn_accept_and_continue)
        lblBottomLine = findViewById(R.id.lbl_bottom_line)

        btnAcceptAndContinue.setOnClickListener(this)
    }

    private fun fillData() {
        introPagerAdapter = IntroPagerAdapter(this@OneTimeSplashActivity)
        viewpagerIntro.adapter = introPagerAdapter

        lblBottomLine.makeLinks(Pair("Terms of Service",View.OnClickListener {
            val intent = Intent(this@OneTimeSplashActivity,WebViewActivity::class.java).apply {
                putExtra("is_privacy_policy",false)
                putExtra("link_to_show","https://www.freeprivacypolicy.com/terms/view/68e31959580b9ad103113b84957ad241")
            }
            startActivity(intent)
        }),Pair("Privacy Policy",View.OnClickListener {
            val intent = Intent(this@OneTimeSplashActivity,WebViewActivity::class.java).apply {
                putExtra("is_privacy_policy",true)
                putExtra("link_to_show","https://www.freeprivacypolicy.com/privacy/view/8778bdd947b6b6b3f262dc023fca77dc")
            }
            startActivity(intent)
        }))
    }
}

private fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
    val spannableString = SpannableString(this.text)
    for (link in links){
        val clickableSpan = object : ClickableSpan(){
            override fun onClick(view : View) {
                Selection.setSelection((view as TextView).text as Spannable,0)
                view.invalidate()
                link.second.onClick(view)
            }
        }
        val startIndexOfLink = this.text.toString().indexOf(link.first)
        spannableString.setSpan(clickableSpan,startIndexOfLink,startIndexOfLink + link.first.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        this.movementMethod = LinkMovementMethod.getInstance()
        this.setText(spannableString,TextView.BufferType.SPANNABLE)
    }
}
