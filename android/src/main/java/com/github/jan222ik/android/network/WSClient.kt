package com.github.jan222ik.android.network

import androidx.lifecycle.LifecycleCoroutineScope
import com.github.jan222ik.common.HandLandmark
import io.ktor.client.HttpClient
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.wss
import io.ktor.http.HttpMethod.Companion.Get
import io.ktor.http.Url
import io.ktor.http.cio.websocket.Frame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class WSClient {
    private lateinit var ws : HttpClient
    private val landmarks : MutableList<HandLandmark> = mutableListOf()

    private suspend fun connect(ktor: HttpClient, u: Url) {
        ktor.wss(Get, u.host, u.port, u.encodedPath) {
            awaitAll(async {
                // TODO send something useful
                outgoing.send(Frame.Text(landmarks.toString()))
                landmarks.clear()
//                ui.button.clicks()
//                    .map { click -> JSONObject()
//                        .put("action", "sendmessage")
//                        .put("data", "Hello from Android!")
//                        .toString()
//                    }
//                    .map { json -> Frame.Text(json) }
//                    .collect { outgoing.send(it) }
            }, async {
//                incoming.consumeEach { frame ->
//                    if (frame is Frame.Text) {
//                        ui.status.append(frame.readText())
//                    }
//                }
            })
        }
    }

    fun connect(lifecycleScope : LifecycleCoroutineScope, urlString : String) {
        val url = Url(urlString)
        ws = HttpClient() {
            install(WebSockets)
        }
        lifecycleScope.launch(Dispatchers.IO) {
            connect(ws, url)
        }
    }

    fun send(markers : List<HandLandmark>){
        landmarks.addAll(markers)
    }
}
