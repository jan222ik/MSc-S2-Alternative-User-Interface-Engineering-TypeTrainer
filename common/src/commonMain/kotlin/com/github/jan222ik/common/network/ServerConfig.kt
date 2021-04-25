package com.github.jan222ik.common.network

object ServerConfig {
    const val PORT = 8080
    const val PROTOCOL_WEBSOCKET = "ws"
    const val PROTOCOL_TEST = "http"
    const val ROUTE_WEBSOCKET = "/ws"
    const val ROUTE_TEST = "/test"

    fun getTestUrl(ip: String): String {
        return StringBuilder()
            .append(PROTOCOL_TEST)
            .append("://")
            .append(ip)
            .append(":")
            .append(PORT)
            .append(ROUTE_TEST)
            .toString()
    }

    fun getWebsocketUrl(ip: String): String {
        return StringBuilder()
            .append(PROTOCOL_WEBSOCKET)
            .append("://")
            .append(ip)
            .append(":")
            .append(PORT)
            .append(ROUTE_WEBSOCKET)
            .toString()
    }
}
