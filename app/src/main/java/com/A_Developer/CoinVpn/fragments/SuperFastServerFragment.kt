package com.A_Developer.CoinVpn.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.anchorfree.hydrasdk.HydraSdk
import com.anchorfree.hydrasdk.api.data.Country
import com.anchorfree.hydrasdk.callbacks.Callback
import com.anchorfree.hydrasdk.exceptions.HydraException
import com.A_Developer.CoinVpn.R
import com.A_Developer.CoinVpn.adapters.CountryListAdapter
import com.A_Developer.CoinVpn.interfaces.OnCountryChooseListner
import com.A_Developer.CoinVpn.pojos.Countries
import com.google.android.material.snackbar.Snackbar

class SuperFastServerFragment : Fragment() {

    private lateinit var superFastServerView : View

    private lateinit var lstCountry : ListView
    private lateinit var relLoadingCountry : RelativeLayout

    private lateinit var countryListAdapter: CountryListAdapter

    private var countryList = mutableListOf<Countries>()
    private var serverPrices = mutableListOf(
        Pair("jp",350),
        Pair("us",400),
        Pair("nl",250),
        Pair("gb",300),
        Pair("au",300),
        Pair("se",250)
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        superFastServerView = inflater.inflate(R.layout.fragment_super_fast_server, container, false)

        initControls()
        fillData()

        lstCountry.setOnItemClickListener{ _: AdapterView<*>, view : View, position: Int, _: Long ->
            val serverPrice = view.getTag(R.string.server_price) as? Int
            onCountryChooseListner!!.onCountryChoose(countryList[position].countryCode!!,serverPrice ?: 0)
            activity!!.finish()
        }

        return superFastServerView
    }

    private fun initControls() {

        lstCountry = superFastServerView.findViewById(R.id.lst_country)
        relLoadingCountry = superFastServerView.findViewById(R.id.rel_loading_country)
    }

    private fun fillData() {
        HydraSdk.countries(object : Callback<List<Country>> {
            override fun success(countries: List<Country>) {
                prepareFinalList(countries)
                showCountry()
            }

            override fun failure(e: HydraException) {
                Snackbar.make(lstCountry,"Counld not load countries", Snackbar.LENGTH_SHORT).show()
            }
        })
    }

    private fun prepareFinalList(countries: List<Country>) {
        countryList.add(Countries("Best Country",false,null,false,null))
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
        countryListAdapter = CountryListAdapter(activity!!,countryList)
        lstCountry.adapter = countryListAdapter
        relLoadingCountry.visibility = View.GONE
    }

    companion object {
        var onCountryChooseListner : OnCountryChooseListner? = null

    }
}
