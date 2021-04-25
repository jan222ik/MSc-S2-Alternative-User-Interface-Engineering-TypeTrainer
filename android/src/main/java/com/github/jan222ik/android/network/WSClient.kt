package com.github.jan222ik.android.network

import androidx.lifecycle.LifecycleCoroutineScope
import com.github.jan222ik.common.HandLandmark
import com.github.jan222ik.common.network.ServerConfig
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

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun connect(ktor: HttpClient, u: Url) {
        ktor.ws(Get, u.host, u.port, u.encodedPath) {
            landmarks.collect {
                send(Frame.Binary(true, Cbor.encodeToByteArray(it)))
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
            scheme = ServerConfig.PROTOCOL_TEST,
            host = ip,
            port = ServerConfig.PORT,
            path = ServerConfig.ROUTE_TEST
        )

        client.close()
        return response.status == HttpStatusCode.OK
    }
}
