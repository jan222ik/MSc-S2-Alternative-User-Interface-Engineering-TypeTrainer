package util

import java.net.NetworkInterface

object NetworkUtils {

    fun getAddress(): String {
        return NetworkInterface.getNetworkInterfaces().toList().flatMap { iNet ->
            iNet.inetAddresses.toList()
                .filter { it.address.size == 4 }
                .filter { !it.isLoopbackAddress }
                .filter { it.address[0] != 10.toByte() }
                .map { it.hostAddress }
        }.first()
    }
}
