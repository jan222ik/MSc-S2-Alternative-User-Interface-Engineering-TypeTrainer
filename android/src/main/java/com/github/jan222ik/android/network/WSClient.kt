package com.github.jan222ik.android.network

import androidx.lifecycle.LifecycleCoroutineScope
import com.github.jan222ik.common.HandLandmark
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.ws
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.Url
import io.ktor.http.cio.websocket.Frame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.encodeToByteArray

class WSClient {
    val landmarks = MutableStateFlow(listOf<HandLandmark>())

    @OptIn(ExperimentalSerializationApi::class)
    private suspend fun connect(ktor: HttpClient, u: Url) {
        ktor.ws(Get, u.host, u.port, u.encodedPath) {
            landmarks.collect {
                send(Frame.Binary(true, Cbor.encodeToByteArray(it)))
            }
        }
    }

    fun connect(lifecycleScope: LifecycleCoroutineScope, urlString: String) {
        val url = Url(urlString)
        val wsClient = HttpClient(OkHttp) {
            install(WebSockets)
        }
        lifecycleScope.launch(Dispatchers.IO) {
            connect(wsClient, url)
        }
    }
}
