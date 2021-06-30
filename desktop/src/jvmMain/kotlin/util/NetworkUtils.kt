package util

import com.github.jan222ik.common.HasDoc
import java.net.InetAddress
import java.net.NetworkInterface

@HasDoc
object NetworkUtils {

    fun getAllAddresses(): List<InetAddress> {
        return NetworkInterface.getNetworkInterfaces().toList()
            .filter { it.isUp }
            .filter { !it.isVirtual }
            .filter { !it.isLoopback }
            .flatMap { iNet ->
                iNet.inetAddresses.toList()
                    .filter { it.address.size == 4 }
                    .filter { it.address[0] != 10.toByte() }
            }
    }

    fun getAddress(): String {
        return getAllAddresses()
            .map { it.hostAddress }
            .first()
    }

    fun getAddressByHost(
        host: String = InetAddress.getLocalHost().hostName
    ): String {
        return InetAddress.getByName(host).hostAddress
    }
}
