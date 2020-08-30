package com.A_Developer.CoinVpn.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.A_Developer.CoinVpn.fragments.ExtraSuperFastServerFragment
import com.A_Developer.CoinVpn.fragments.SuperFastServerFragment

class ServerCountryPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private var fragmentList = mutableListOf(
        SuperFastServerFragment(),
        ExtraSuperFastServerFragment()
    )
    private var fragmentTitle = mutableListOf("Super Fast Server","Extra Super Fast Server")

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return fragmentTitle[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}