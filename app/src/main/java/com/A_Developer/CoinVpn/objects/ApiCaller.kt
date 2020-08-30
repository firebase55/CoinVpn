package com.A_Developer.CoinVpn.objects

import com.A_Developer.CoinVpn.interfaces.IpInfoInterface
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiCaller {
    private const val BASE_URL = "http://www.ip-api.com/"

    private val httpClient: OkHttpClient
        get() {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            return OkHttpClient.Builder().apply {
                addInterceptor(interceptor)
                connectTimeout(3, TimeUnit.MINUTES)
                readTimeout(3, TimeUnit.MINUTES)
                writeTimeout(3, TimeUnit.MINUTES)
            }.build()
        }
    private val interfaceBuilder: IpInfoInterface
        get() {
            val retrofit = Retrofit.Builder().apply {
                baseUrl(BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
                client(httpClient)
            }.build()
            return retrofit.create(IpInfoInterface::class.java)
        }

    fun postRequest(url : String, handler : (response : ResponseBody?, error : String?)-> Unit){
        val call = interfaceBuilder.getIpInfo(url).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                handler.invoke(response.body(),null)
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                handler.invoke(null,t.message.toString())
            }
        })
    }
}