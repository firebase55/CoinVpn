package com.A_Developer.CoinVpn.handlers

import android.location.Location
import android.os.StrictMode
import com.A_Developer.CoinVpn.pojos.Geo
import com.A_Developer.CoinVpn.pojos.NearestServers
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.net.URL


class GetNearestServer : Thread(){
    private var geo : Geo? = null
    private val listOfServers = mutableListOf<NearestServers>()
    var nearestServer = NearestServers()
    var isFinished = false
    var lat : Double? = null
    var lon : Double? = null

    override fun run() {
        getMyServer()
        getAllServers()
        filterResults()
        isFinished = true
        interrupt()
    }

    private fun getMyServer() {
        try {
            val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
            StrictMode.setThreadPolicy(policy)

            val parserFactory = XmlPullParserFactory.newInstance()
            val parser = parserFactory.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            val url = URL("https://www.speedtest.net/speedtest-config.php")
            parser.setInput(url.openConnection().getInputStream(),null)

            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT){
                val name = parser.name
                when (eventType) {
                    XmlPullParser.END_TAG -> {
                        if (name == "client"){
                            lat = parser.getAttributeValue(null,"lat").toDouble()
                            lon = parser.getAttributeValue(null,"lon").toDouble()
                        }
                    }
                }
                eventType = parser.next()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    @Throws(IOException::class, XmlPullParserException::class)
    private fun getAllServers() {
        val parserFactory = XmlPullParserFactory.newInstance()
        val parser = parserFactory.newPullParser()
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
        val url = URL("https://www.speedtest.net/speedtest-servers-static.php")
        parser.setInput(url.openConnection().getInputStream(),null)

        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            val name = parser.name
            when (eventType) {
                XmlPullParser.END_TAG -> {
                    if (name == "server") {
                        val city = parser.getAttributeValue(null, "name")
                        //if (city.toLowerCase(Locale.getDefault()).contains(geo!!.city!!.toLowerCase(Locale.getDefault()))) {
                            val lat = parser.getAttributeValue(null, "lat").toDouble()
                            val lon = parser.getAttributeValue(null, "lon").toDouble()
                            val host = parser.getAttributeValue(null, "host").toString()
                            val serverName = parser.getAttributeValue(null, "sponsor")
                            val url = parser.getAttributeValue(null, "url")
                            listOfServers.add(NearestServers(lat, lon, host, serverName, url))
                        //}
                    }
                }
            }
            eventType = parser.next()
        }
    }

    private fun filterResults() {
        var tmp = 19349458.0
        var dist = 0.0
        for (i in 0 until listOfServers.size) {
            val source = Location("Source")
            source.latitude = lat!!
            source.longitude = lon!!

            val dest = Location("Dest")
            dest.latitude = listOfServers[i].lat!!
            dest.longitude = listOfServers[i].lon!!

            val distance = source.distanceTo(dest).toDouble()
            if (tmp > distance) {
                tmp = distance
                dist = distance
                nearestServer = listOfServers[i]
            }
        }
    }
}