package com.github.jan222ik.android.network

import android.content.Context.WIFI_SERVICE
import android.net.wifi.WifiManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.github.jan222ik.common.network.NDService
import io.ktor.utils.io.core.ByteOrder
import java.math.BigInteger
import java.net.InetAddress
import java.net.UnknownHostException
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceListener

object NDSDiscovery {
    @Composable
    fun start(callback: (ip: String) -> Unit) {
        val jmdns = JmDNS.create(getLocalWifiIpAddress(), NDService.SERVICE_NAME)
        jmdns.addServiceListener(
            NDService.SERVICE_TYPE,
            SampleListener(callback) {
                jmdns.unregisterAllServices()
                jmdns.close()
            })
    }

    private class SampleListener(externCallback: (ip: String) -> Unit, stopCall: () -> Unit) : ServiceListener {
        val callback: (value: String) -> Unit = externCallback
        val stop: () -> Unit = stopCall

        override fun serviceAdded(event: ServiceEvent) {
        }

        override fun serviceRemoved(event: ServiceEvent) {
        }

        override fun serviceResolved(event: ServiceEvent) {
            println("BOING-----------------------------------------------")
            callback(event.info.hostAddresses!![0])
            stop
        }
    }

    @Composable
    fun getLocalWifiIpAddress(): InetAddress? {
        val wifiManager = LocalContext.current.getSystemService(WIFI_SERVICE) as WifiManager
        var ipAddress = wifiManager.connectionInfo.ipAddress
        if (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            ipAddress = Integer.reverseBytes(ipAddress)
        }
        val ipByteArray: ByteArray = BigInteger.valueOf(ipAddress.toLong()).toByteArray()
        return try {
            InetAddress.getByAddress(ipByteArray)
        } catch (ex: UnknownHostException) {
            null
        }
    }
}
