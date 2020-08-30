package com.A_Developer.CoinVpn.interfaces

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Url

interface IpInfoInterface {

    @POST
    fun getIpInfo(@Url url : String) : Call<ResponseBody>
}