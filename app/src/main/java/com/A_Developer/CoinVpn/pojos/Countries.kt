package com.A_Developer.CoinVpn.pojos

class Countries {
    var countryCode : String? = null
    var haveServer : Boolean? = null
    var noOfServer : Int? = null
    var haveServerPrice : Boolean? = null
    var serverPrice : Int? = null

    constructor(countryCode: String?, haveServer: Boolean?, noOfServer: Int?, haveServerPrice: Boolean?, serverPrice: Int?) {
        this.countryCode = countryCode
        this.haveServer = haveServer
        this.noOfServer = noOfServer
        this.haveServerPrice = haveServerPrice
        this.serverPrice = serverPrice
    }
}