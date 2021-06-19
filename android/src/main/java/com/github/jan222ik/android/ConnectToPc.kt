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

    NDSDiscovery.start(callback = {
        coroutineScope.launch(Dispatchers.IO) {
            if (WSClient.canConnect(it)) {
                WSClient.url = ServerConfig.getWebsocketUrl(it)
                WSClient.loadWeekly(it, sharedPref)
                router.navTo(MobileRoutes.Exercise(Connection(true)))
            }
        }
    })

    Column {
        val text = remember { mutableStateOf("") }
        TextField(text.value, onValueChange = text.component2())
        Button(
            onClick = {
                GlobalScope.launch(Dispatchers.IO) {
                    val ip = text.value
                    if (WSClient.canConnect(ip)) {
                        WSClient.url = ServerConfig.getWebsocketUrl(ip)
                        WSClient.loadWeekly(ip, sharedPref)
                        router.navTo(MobileRoutes.Exercise(Connection(true)))
                    }
                }
            }
        ) {
            Text(text = "Try from Textbox")
        }
    }
}


//    val router = MobileRouterAmbient.current
//
//    val lifecycleOwner = LocalLifecycleOwner.current
//    val context = LocalContext.current
//    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
//
//    AndroidView(
//        modifier = Modifier.fillMaxSize(),
//        factory = { ctx ->
//            val previewView = PreviewView(ctx).apply {
//                this.scaleType = PreviewView.ScaleType.FILL_CENTER
//                layoutParams = ViewGroup.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT,
//                    ViewGroup.LayoutParams.MATCH_PARENT
//                )
//                // Preview is incorrectly scaled in Compose on some devices without this
//                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
//            }
//            val executor = ContextCompat.getMainExecutor(ctx)
//            cameraProviderFuture.addListener({
//                val cameraProvider = cameraProviderFuture.get()
//                val preview = Preview.Builder().build().also {
//                    it.setSurfaceProvider(previewView.surfaceProvider)
//                }
//
//                val cameraSelector = CameraSelector.Builder()
//                    .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                    .build()
//
//                val imageAnalysis =
//                    ImageAnalysis.Builder()
//                        .setTargetResolution(Size(previewView.width, previewView.height))
//                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                        .build()
//                        .also {
//                            it.setAnalyzer(
//                                Executors.newSingleThreadExecutor(),
//                                QrCodeAnalyzer { qrResult ->
//                                    println(qrResult.text)
//                                    router.navTo(MobileRoutes.Exercise(Connection(qrResult.text)))
//                                }
//                            )
//                        }
//
//                cameraProvider.unbindAll()
//                cameraProvider.bindToLifecycle(
//                    lifecycleOwner,
//                    cameraSelector,
//                    preview,
//                    imageAnalysis
//                )
//            }, executor)
//            previewView
//        },
//    )

