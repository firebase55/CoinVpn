package com.A_Developer.CoinVpn.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.A_Developer.CoinVpn.R
import com.A_Developer.CoinVpn.adapters.CountryListAdapter
import com.A_Developer.CoinVpn.interfaces.OnCountryChooseListner
import com.A_Developer.CoinVpn.pojos.Countries

class ExtraSuperFastServerFragment : Fragment() {

    private lateinit var extraSuperFastServerView : View

    private lateinit var lstCountry : ListView
    private lateinit var relLoadingCountry : RelativeLayout

    private val countries = mutableListOf<Country>()
    private var countryList = mutableListOf<Countries>()
    private var serverPrices = mutableListOf(
        Pair("hk",500),
        Pair("id",550),
        Pair("ro",600),
        Pair("ch",500),
        Pair("ar",550),
        Pair("de",650)
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        extraSuperFastServerView = inflater.inflate(R.layout.fragment_extra_super_fast_server, container, false)

        initControls()
        prepareList()
        showCountry()

        lstCountry.setOnItemClickListener{ _: AdapterView<*>, view : View, position: Int, _: Long ->
            val serverPrice = view.getTag(R.string.server_price) as? Int
            onCountryChooseListener!!.onCountryChoose(countryList[position].countryCode!!,serverPrice ?: 0)
            activity!!.finish()
        }

        return extraSuperFastServerView
    }

    private fun initControls() {
        lstCountry = extraSuperFastServerView.findViewById(R.id.lst_country)
        relLoadingCountry = extraSuperFastServerView.findViewById(R.id.rel_loading_country)

        countries.add(Country("hk", 5))
        countries.add(Country("id", 3))
        countries.add(Country("ro", 5))
        countries.add(Country("ch", 2))
        countries.add(Country("ar", 7))
        countries.add(Country("de", 5))
    }

    private fun prepareList() {
        for (i in countries.indices){
            var isAddedCountry = true
            for (j in serverPrices.indices){
                if(countries[i].country.toLowerCase() == serverPrices[j].first.toLowerCase()){
                    isAddedCountry = false
                    countryList.add(Countries(countries[i].country,true,countries[i].servers,true,serverPrices[j].second))
                }
            }
            if(isAddedCountry){
                countryList.add(Countries(countries[i].country,true,countries[i].servers,false,null))
            }
        }
    }

    private fun showCountry() {
        lstCountry.adapter = CountryListAdapter(activity!!,countryList)
        relLoadingCountry.visibility = View.GONE
    }

    companion object {
        var onCountryChooseListener : OnCountryChooseListner? = null
    }

    data class Country(var country : String,var servers : Int)

}
