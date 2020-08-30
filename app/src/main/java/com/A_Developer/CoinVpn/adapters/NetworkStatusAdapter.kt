package com.A_Developer.CoinVpn.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.A_Developer.CoinVpn.R
import com.A_Developer.CoinVpn.pojos.NetworkStatus

class NetworkStatusAdapter : BaseAdapter{
    private var listOfNetworkStatus : MutableList<NetworkStatus>? = null
    private var context : Context? = null

    private var layoutInflater : LayoutInflater? = null
    private var rowView : View? = null

    constructor(listOfNetworkStatus: MutableList<NetworkStatus>?, context: Context?) : super() {
        this.listOfNetworkStatus = listOfNetworkStatus
        this.context = context

        layoutInflater = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        rowView = convertView ?: layoutInflater!!.inflate(R.layout.simple_layout,null)

        val progressNetwork = rowView!!.findViewById<ProgressBar>(R.id.progress_network)
        val imgNetworkChecking = rowView!!.findViewById<ImageView>(R.id.img_network_checking)
        val lblNetworkChecking = rowView!!.findViewById<TextView>(R.id.lbl_network_checking)

        val networkStatus = getItem(position) as NetworkStatus

        if(networkStatus.isCompleteCheck!!){
            progressNetwork.visibility = View.GONE
            imgNetworkChecking.visibility = View.VISIBLE
        } else {
            progressNetwork.visibility = View.VISIBLE
            imgNetworkChecking.visibility = View.GONE
        }
        lblNetworkChecking.text = networkStatus.networkStatus
        return rowView!!
    }

    override fun getItem(position: Int): Any {
        return listOfNetworkStatus!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return listOfNetworkStatus!!.size
    }
}