package com.github.jan222ik.common.network

object ServerConfig {
    const val PORT = 8080
    const val PROTOCOL_WEBSOCKET = "ws"
    const val PROTOCOL_HTTP = "http"
    const val ROUTE_WEBSOCKET = "/ws"
    const val ROUTE_TEST = "/test"
    const val ROUTE_FETCH_WEEKLY = "/stats-for-mobile"

    fun getTestUrl(ip: String): String {
        return StringBuilder()
            .append(PROTOCOL_HTTP)
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

    fun getWeeklyUrl(ip: String): String {
        return StringBuilder()
            .append(PROTOCOL_HTTP)
            .append("://")
            .append(ip)
            .append(":")
            .append(PORT)
            .append(ROUTE_FETCH_WEEKLY)
            .toString()
    }
}
