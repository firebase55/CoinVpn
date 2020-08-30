package com.A_Developer.CoinVpn.pojos

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

class Geo : Serializable {
    @JsonProperty("as") var asa : String? = null
    @JsonProperty("city") var city : String? = null
    @JsonProperty("country") var country : String? = null
    @JsonProperty("countryCode") var countryCode : String? = null
    @JsonProperty("isp") var isp : String? = null
    @JsonProperty("lat") var lat : String? = null
    @JsonProperty("lon") var lon : String? = null
    @JsonProperty("org") var org : String? = null
    @JsonProperty("query") var ip : String? = null
    @JsonProperty("region") var region : String? = null
    @JsonProperty("regionName") var regionName : String? = null
    @JsonProperty("status") var status : String? = null
    @JsonProperty("timezone") var timezone : String? = null
    @JsonProperty("zip") var zip : String? = null
}