package com.github.jan222ik.android.network

import androidx.compose.runtime.Composable
import com.github.jan222ik.common.network.NDService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceListener


object NDSDiscovery {
    @Composable
    fun start(callback: (ip: String) -> Unit): Boolean {
        val localAddresses = getLocalIpAddresses()

        if (localAddresses.isNotEmpty()) {
            localAddresses.forEach { address ->
                GlobalScope.launch(Dispatchers.IO) {
                    val jmdns = JmDNS.create(address, NDService.SERVICE_NAME)
                    jmdns.addServiceListener(
                        NDService.SERVICE_TYPE,
                        ConnectionListener(callback) {
                            jmdns.unregisterAllServices()
                            jmdns.close()
                        })
                }
            }
            return true
        }
        return false
    }

    private class ConnectionListener(externCallback: (ip: String) -> Unit, stopCall: () -> Unit) : ServiceListener {
        val callback: (value: String) -> Unit = externCallback
        val stop: () -> Unit = stopCall

        override fun serviceAdded(event: ServiceEvent) {
        }

        override fun serviceRemoved(event: ServiceEvent) {
        }

        override fun serviceResolved(event: ServiceEvent) {
            callback(event.info.hostAddresses!![0])
            stop
        }
    }

    private fun getLocalIpAddresses(): List<InetAddress> {
        val ipList = mutableListOf<InetAddress>()
        try {
            val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val enumIpAddr: Enumeration<InetAddress> = en.nextElement().inetAddresses
                while (enumIpAddr.hasMoreElements()) {
                    val inetAddress: InetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                        ipList.add(inetAddress)
                    }
                }
            }
        } catch (ex: SocketException) {
            System.err.println(ex)
        }
        return ipList
    }
}
