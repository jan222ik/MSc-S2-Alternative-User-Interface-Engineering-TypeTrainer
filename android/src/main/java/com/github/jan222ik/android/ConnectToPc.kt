@file:Suppress("FunctionName")

package com.github.jan222ik.android

import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.github.jan222ik.android.network.NDSDiscovery
import com.github.jan222ik.android.network.WSClient
import com.github.jan222ik.common.network.ServerConfig
import com.github.jan222ik.common.ui.router.Connection
import com.github.jan222ik.common.ui.router.MobileRouterAmbient
import com.github.jan222ik.common.ui.router.MobileRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun ConnectToPc(sharedPref: SharedPreferences) {
    val router = MobileRouterAmbient.current
    val coroutineScope = rememberCoroutineScope()

    val started = NDSDiscovery.start(callback = {
        coroutineScope.launch(Dispatchers.IO) {
            if (WSClient.canConnect(it)) {
                WSClient.router = router
                WSClient.url = ServerConfig.getWebsocketUrl(it)
                WSClient.loadWeekly(it, sharedPref)
                router.navTo(MobileRoutes.Exercise(Connection(canConnect = true)))
            }
        }
    })

    if (started)
        Column {
            val text = remember { mutableStateOf("") }
            TextField(text.value, onValueChange = text.component2())
            Button(onClick = {
                GlobalScope.launch(Dispatchers.IO) {
                    val ip = text.value
                    if (WSClient.canConnect(ip)) {
                        WSClient.url = ServerConfig.getWebsocketUrl(ip)
                        WSClient.loadWeekly(ip, sharedPref)
                        router.navTo(MobileRoutes.Exercise(Connection(true)))
                    }
                }
            }) {
                Text(text = "Try from Textbox")
            }
        }
    else
        router.back()
}

