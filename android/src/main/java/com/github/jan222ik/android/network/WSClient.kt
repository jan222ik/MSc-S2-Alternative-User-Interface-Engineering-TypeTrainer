package com.github.jan222ik.android.network

import android.content.SharedPreferences
import androidx.lifecycle.LifecycleCoroutineScope
import com.github.jan222ik.common.HandLandmark
import com.github.jan222ik.common.dto.SHARED_STATS_PREF_KEY
import com.github.jan222ik.common.network.ServerConfig
import com.github.jan222ik.common.ui.router.MobileRoutes
import com.github.jan222ik.common.ui.util.router.Router
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.cio.websocket.Frame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.encodeToByteArray

object WSClient {
    val landmarks = MutableStateFlow(listOf<HandLandmark>())
    lateinit var url: String
    lateinit var router: Router<MobileRoutes>

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun connect(ktor: HttpClient, u: Url) {
        ktor.ws(Get, u.host, u.port, u.encodedPath) {
            landmarks.collect {
                try {
                    send(Frame.Binary(true, Cbor.encodeToByteArray(it)))
                } catch (ex: Exception) {
                    router.previous
                }
            }
        }
    }

    fun connect(lifecycleScope: LifecycleCoroutineScope) {
        val url = Url(url)
        val wsClient = HttpClient(OkHttp) {
            install(WebSockets)
        }
        lifecycleScope.launch(Dispatchers.IO) {
            connect(wsClient, url)
        }
    }

    suspend fun canConnect(ip: String): Boolean {
        val client = HttpClient(OkHttp)
        val response: HttpResponse = client.get(
            scheme = ServerConfig.PROTOCOL_HTTP,
            host = ip,
            port = ServerConfig.PORT,
            path = ServerConfig.ROUTE_TEST
        )

        client.close()
        return response.status == HttpStatusCode.OK
    }

    suspend fun loadWeekly(ip: String, sharedPref: SharedPreferences) {
        val client = HttpClient(OkHttp)
        val response: HttpResponse = client.get(
            scheme = ServerConfig.PROTOCOL_HTTP,
            host = ip,
            port = ServerConfig.PORT,
            path = ServerConfig.ROUTE_FETCH_WEEKLY
        )
        client.close()
        if (response.status == HttpStatusCode.OK) {
            val json = response.toString()
            println("json = $json")
            with(sharedPref.edit()) {
                putString(SHARED_STATS_PREF_KEY, json)
                apply()
            }
        } else {
            System.err.println("Failed to fetch weekly data!")
        }
    }
}
