package com.A_Developer.CoinVpn.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.A_Developer.CoinVpn.R

class IntroPagerAdapter : PagerAdapter {
    private var context : Context? = null
    private val imageList = mutableListOf(R.drawable.intro_one,R.drawable.intro_two,R.drawable.intro_three,R.drawable.intro_four)
    constructor(context: Context?) : super() {
        this.context = context
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as View
    }

    override fun getCount(): Int {
        return imageList.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        imageView.setImageResource(imageList[position])
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}