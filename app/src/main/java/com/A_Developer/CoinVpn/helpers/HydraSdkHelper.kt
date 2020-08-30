package com.A_Developer.CoinVpn.helpers

import android.widget.Toast
import com.anchorfree.hydrasdk.HydraSdk
import com.anchorfree.hydrasdk.SessionConfig
import com.anchorfree.hydrasdk.api.data.ServerCredentials
import com.anchorfree.hydrasdk.api.response.RemainingTraffic
import com.anchorfree.hydrasdk.callbacks.Callback
import com.anchorfree.hydrasdk.callbacks.CompletableCallback
import com.anchorfree.hydrasdk.exceptions.*
import com.anchorfree.hydrasdk.fireshield.AlertPage
import com.anchorfree.hydrasdk.fireshield.FireshieldCategory
import com.anchorfree.hydrasdk.fireshield.FireshieldCategoryRule
import com.anchorfree.hydrasdk.fireshield.FireshieldConfig
import com.anchorfree.hydrasdk.vpnservice.VPNState
import com.anchorfree.reporting.TrackingConstants
import com.A_Developer.CoinVpn.app.VpnApp

class HydraSdkHelper {

    private var dbHelper = DBHelper(VpnApp.appContext)
    private val sharedPrefHelper = SharedPrefHelper()

    fun connectToVPN(countryCode: String, serverPrice: Int,handler: (isSuccess: Boolean) -> Unit){
        HydraSdk.startVPN(SessionConfig.Builder().apply {
            withReason(TrackingConstants.GprReasons.M_UI)
            withFireshieldConfig(createFireShieldConfig())
            withVirtualLocation(countryCode)
        }.build(),object : Callback<ServerCredentials> {

            override fun success(p0: ServerCredentials) {
                sharedPrefHelper
                    .putLong(SharedPrefHelper.CONNECT_TIME,System.currentTimeMillis())
                sharedPrefHelper
                    .putString(SharedPrefHelper.LASTCONNECTED_COUNTRY,countryCode)
                dbHelper.deductCoin(serverPrice)
                handler.invoke(true)
            }

            override fun failure(e: HydraException) {
                handleError(e)
                handler.invoke(false)
            }
        })
    }

    fun connectVPNToOptimalLocation(handler : (isSuccess : Boolean) -> Unit){
        HydraSdk.startVPN(SessionConfig.Builder().apply {
            withVirtualLocation(HydraSdk.COUNTRY_OPTIMAL)
            withReason(TrackingConstants.GprReasons.M_UI)
            withFireshieldConfig(createFireShieldConfig())
        }.build(), object : Callback<ServerCredentials> {
            override fun success(serverCredentials: ServerCredentials) {
                sharedPrefHelper
                    .putLong(SharedPrefHelper.CONNECT_TIME,System.currentTimeMillis())
                sharedPrefHelper
                    .putString(SharedPrefHelper.LASTCONNECTED_COUNTRY,"Best Country")
                handler.invoke(true)

            }

            override fun failure(e: HydraException) {
                handleError(e)
                handler.invoke(false)
            }
        })
    }

    fun disconnectFromVPN(handler: (isSuccess: Boolean) -> Unit){
        HydraSdk.stopVPN(TrackingConstants.GprReasons.M_UI,object : CompletableCallback {
            override fun complete() {
                sharedPrefHelper
                    .putLong(SharedPrefHelper.DISCONNECT_TIME,System.currentTimeMillis())
                sharedPrefHelper
                    .putString(SharedPrefHelper.LASTCONNECTED_COUNTRY,"Best Country")
                handler.invoke(true)
            }

            override fun error(e: HydraException) {
                handleError(e)
                handler.invoke(false)
            }
        })
    }

    fun disconnectFromVPNSimple(handler: (isSuccess: Boolean) -> Unit){
        HydraSdk.stopVPN(TrackingConstants.GprReasons.M_UI,object : CompletableCallback {
            override fun complete() {
                handler.invoke(true)
            }

            override fun error(e: HydraException) {
                handleError(e)
                handler.invoke(false)
            }
        })
    }

    fun remainingTraffic(handler: (isUnlimited: Boolean?,trafficUsed : Long?,trafficLimit : Long?) -> Unit){
        HydraSdk.remainingTraffic(object : Callback<RemainingTraffic>{
            override fun success(remainingTraffic: RemainingTraffic) {
                handler.invoke(remainingTraffic.isUnlimited,remainingTraffic.trafficUsed,remainingTraffic.trafficLimit)
            }

            override fun failure(e: HydraException) {
                handleError(e)
                handler.invoke(null,null,null)
            }
        })
    }

    fun isConnected(): Boolean {
        var isConnected = false
        HydraSdk.getVpnState(object : Callback<VPNState> {
            override fun success(vpnState: VPNState) {
                isConnected = vpnState == VPNState.CONNECTED
            }

            override fun failure(e: HydraException) {
                handleError(e)
            }

        })
        return isConnected
    }

    fun isConnecting(): Boolean {
        var isConnecting = false
        HydraSdk.getVpnState(object : Callback<VPNState>{
            override fun success(vpnState: VPNState) {
                isConnecting = vpnState == VPNState.CONNECTING_VPN
            }

            override fun failure(e: HydraException) {
                handleError(e)
            }
        })
        return isConnecting
    }
    fun handleError(e: Throwable) {
        when (e) {
            is NetworkRelatedException -> showToast("Check Internet Connection")

            is VPNException -> when ((e ).code){
                VPNException.REVOKED -> {
                    showToast("User revoked vpn permissions")
                }
                VPNException.VPN_PERMISSION_DENIED_BY_USER -> {
                    showToast("VPN permission denied")
                }
                VPNException.HYDRA_ERROR_BROKEN -> {
                    showToast("Connection with vpn service was lost")
                }
                VPNException.HYDRA_DCN_BLOCKED_BW -> {
                    showToast("Client traffic exceeded")
                }
                else -> {
                    showToast("Error in VPN Service")
                }
            }

            is ApiHydraException -> when((e).content){
                RequestException.CODE_NOT_AUTHORIZED -> {
                    showToast("User unauthorized")
                }
                RequestException.CODE_TRAFFIC_EXCEED -> {
                    showToast("Server unavailable")
                } else -> {
                    showToast("Other error happen try after some time")
                }
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(VpnApp.appContext,message, Toast.LENGTH_SHORT).show()
    }

    private fun createFireShieldConfig(): FireshieldConfig {
        val builder = FireshieldConfig.Builder()
        builder.enabled(true)
        builder.alertPage(AlertPage.create("connect.bitdefender.net", "safe_zone.html"))
        if(sharedPrefHelper.getBoolean(SharedPrefHelper.MALWARE_SPYWARE)){
            builder.addCategory(FireshieldCategory.Builder.block(FireshieldConfig.Categories.MALWARE))
            builder.addCategoryRule(FireshieldCategoryRule.Builder.fromAssets(FireshieldConfig.Categories.MALWARE,"malware-domains.txt"))
        }
        return builder.build()
    }
}