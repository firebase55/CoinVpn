package com.A_Developer.CoinVpn.pojos

class NetworkStatus {
    var isCompleteCheck : Boolean? = null
    var networkStatus : String? = null

    constructor(isCompleteCheck: Boolean?, networkStatus: String?) {
        this.isCompleteCheck = isCompleteCheck
        this.networkStatus = networkStatus
    }
}