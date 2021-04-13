@file:Suppress("FunctionName")

package com.github.jan222ik.android

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.jan222ik.common.ui.MobileMenu
import com.github.jan222ik.common.ui.components.TypeTrainerTheme
import com.github.jan222ik.common.ui.router.Connection
import com.github.jan222ik.common.ui.router.MobileRouter
import com.github.jan222ik.common.ui.router.MobileRouterAmbient
import com.github.jan222ik.common.ui.router.MobileRoutes

@Composable
fun mobileMain(activity: Activity) {
    TypeTrainerTheme {
        Surface(color = MaterialTheme.colors.background) {
            Box(modifier = Modifier.fillMaxSize()) {
                val initialRoute: MobileRoutes = MobileRoutes.Menu
                MobileRouter(
                    initialRoute = initialRoute
                ) { current, router ->
                    when (current) {
                        MobileRoutes.Menu -> MobileMenu()
                        MobileRoutes.Scanner -> CurrentlyMissing("Scanner")
                        is MobileRoutes.Exercise -> HandTracking(activity)
                        MobileRoutes.CameraSetup -> Column {
                            CurrentlyMissing("Camera Setup")
                            Button(onClick = {
                                router.navTo(MobileRoutes.Exercise(Connection(Any())))
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

