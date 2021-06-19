@file:Suppress("FunctionName")

package com.github.jan222ik.android

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.github.jan222ik.common.ui.MobileMenu
import com.github.jan222ik.common.ui.components.TypeTrainerTheme
import com.github.jan222ik.common.ui.router.Connection
import com.github.jan222ik.common.ui.router.MobileRouter
import com.github.jan222ik.common.ui.router.MobileRouterAmbient
import com.github.jan222ik.common.ui.router.MobileRoutes
import com.github.jan222ik.common.ui.util.router.Router

@Composable
fun mobileMain(activity: Activity, supplyRouter: @Composable (Router<MobileRoutes>) -> Unit) {
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    TypeTrainerTheme {
        Surface(color = MaterialTheme.colors.background) {
            Box(modifier = Modifier.fillMaxSize()) {
                val initialRoute: MobileRoutes = MobileRoutes.Menu
                MobileRouter(
                    initialRoute = initialRoute
                ) { current, router ->
                    supplyRouter(router)
                    when (current) {
                        MobileRoutes.Menu -> MobileMenu(activity)
                        MobileRoutes.Scanner -> ConnectToPc(sharedPref)
                        is MobileRoutes.Exercise -> {
                            if(current.connection.canConnect)
                                HandTracking(activity)
                            else
                                ConnectToPc(sharedPref)
                        }
                        MobileRoutes.CameraSetup -> Column {
                            CurrentlyMissing("Camera Setup")
                            Button(onClick = {
                                router.navTo(MobileRoutes.Exercise(Connection()))
                            }) {
                                Text("Exercise")
                            }
                        }
                        MobileRoutes.AppBenefits -> CurrentlyMissing("App Benefits")
                    }
                }
            }
        }
    }
}

@Composable
fun HandTracking(activity: Activity) {
    val intent = Intent(activity, HandTrackingActivity::class.java)
    activity.startActivity(intent)
}

@Composable
private fun CurrentlyMissing(
    title: String
) {
    val router = MobileRouterAmbient.current
    Column {
        Text(text = "Screen $title is not implemented.")
        Button(
            onClick = router::back
        ) {
            Text(text = "Back")
        }
    }
}

