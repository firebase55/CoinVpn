package com.A_Developer.CoinVpn.pojos

class NearestServers {
    var lat : Double? = null
    var lon : Double? = null
    var host : String? = null
    var serverName : String? = null
    var url : String? = null

    constructor(lat: Double?, lon: Double?, host: String?, serverName: String?, url: String?) {
        this.lat = lat
        this.lon = lon
        this.host = host
        this.serverName = serverName
        this.url = url
    }

    constructor()

}