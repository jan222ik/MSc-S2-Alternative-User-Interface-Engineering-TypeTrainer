package com.github.jan222ik.desktop.network

import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.common.network.NDService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.github.jan222ik.desktop.util.NetworkUtils
import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo

enum class NDSState {
    STARTING, STARTED
}

@HasDoc
object NDSServer {
    private var stopping: Boolean = false
    lateinit var coroutineScope: CoroutineScope

    fun start(server: Server) {
        var port = 25500
        NetworkUtils.getAllAddresses().forEach { address ->
            coroutineScope.launch(Dispatchers.IO) {
                startNDS(server, address, port++)
            }
        }
    }

    fun stop() {
        stopping = true
    }

    private fun startNDS(server: Server, address: InetAddress, port: Int) {
        // Create a JmDNS instance
        val jmdns: JmDNS = JmDNS.create(address, NDService.SERVICE_NAME)
        // Register a service
        val serviceInfo: ServiceInfo =
            ServiceInfo.create(NDService.SERVICE_TYPE, NDService.SERVICE_NAME, port, address.hostAddress)
        jmdns.registerService(serviceInfo)

        while (!server.connectionStatus.value && !stopping) {
            Thread.sleep(1000)
        }

        // Unregister all services
        jmdns.unregisterAllServices()
        jmdns.close()
    }
}
