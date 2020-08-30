package com.A_Developer.CoinVpn.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.A_Developer.CoinVpn.R
import com.A_Developer.CoinVpn.helpers.SharedPrefHelper
import com.A_Developer.CoinVpn.pojos.CommonClass
import com.A_Developer.CoinVpn.pojos.Countries
import java.util.*

class CountryListAdapter : BaseAdapter{
    var context : Context
    var countryList : MutableList<Countries>

    private var layoutInflater : LayoutInflater
    var rowView : View? = null
    var position : Int = 0
    val sharedPrefHelper = SharedPrefHelper()
    constructor(context: Context, countryList: MutableList<Countries>) {
        this.context = context
        this.countryList = countryList

        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        this.position = position
        rowView = convertView ?: layoutInflater.inflate(R.layout.country_listitem,null)
        populateMainContent()
        return rowView!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItem(position: Int): Any {
        return countryList[position]
    }

    override fun getCount(): Int {
        return countryList.size
    }
}

fun CountryListAdapter.populateMainContent() {
    val lblCountryFlag = rowView!!.findViewById<TextView>(R.id.lbl_country_flag)
    val lblCountryName = rowView!!.findViewById<TextView>(R.id.lbl_country_name)
    val lblCountryServerCount = rowView!!.findViewById<TextView>(R.id.lbl_country_server_count)
    val imgIsConnected = rowView!!.findViewById<ImageView>(R.id.img_is_connected)
    val lblServerPrice = rowView!!.findViewById<TextView>(R.id.lbl_server_price)
    val imgMoney = rowView!!.findViewById<ImageView>(R.id.img_money)

    val country = getItem(position) as Countries
    val currentCountry = sharedPrefHelper.getString(SharedPrefHelper.LASTCONNECTED_COUNTRY)

    if(country.countryCode!! == "Best Country"){
        lblCountryFlag.text = "\uD83C\uDF0E"
        lblCountryName.text = "Best Country"
    } else {
        val locale = Locale("",country.countryCode)
        lblCountryFlag.text = CommonClass().getCountryFlag(country.countryCode!!)
        lblCountryName.text = locale.displayName
    }

    if(country.haveServer!!){
        lblCountryServerCount.text = "${country.noOfServer} Servers"
        lblCountryServerCount.visibility = View.VISIBLE
    } else {
        lblCountryServerCount.visibility = View.GONE
    }

    if(country.haveServerPrice!!){
        lblServerPrice.text = country.serverPrice.toString()
        lblServerPrice.visibility = View.VISIBLE
        imgMoney.visibility = View.VISIBLE
        rowView!!.setTag(R.string.server_price,country.serverPrice)
    } else {
        lblServerPrice.visibility = View.GONE
        imgMoney.visibility = View.GONE
    }

    if(currentCountry == country.countryCode){
        imgIsConnected.visibility = View.VISIBLE
        lblServerPrice.visibility = View.GONE
        imgMoney.visibility = View.GONE
    } else {
        imgIsConnected.visibility = View.GONE
    }
}
