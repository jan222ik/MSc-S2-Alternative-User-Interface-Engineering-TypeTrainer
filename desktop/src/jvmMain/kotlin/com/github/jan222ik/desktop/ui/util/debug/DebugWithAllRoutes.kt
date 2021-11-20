@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.util.debug

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.github.jan222ik.desktop.textgen.database.DEMO
import com.github.jan222ik.desktop.ui.dashboard.ApplicationRoutes
import com.github.jan222ik.desktop.ui.general.WindowRouterAmbient
import kotlin.reflect.KClass


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DebugWithAllRoutes() {
    val router = WindowRouterAmbient.current
    LazyColumn {
        stickyHeader {
            Surface {
                Text("Routes:")
            }
        }
        items(getDebugPaths(ApplicationRoutes::class)) {
            when (it.objectInstance) {
                ApplicationRoutes.Dashboard(),
                ApplicationRoutes.Settings,
                ApplicationRoutes.User.Login,
                    //ApplicationRoutes.Exercise.Connection.QRCode,
                    //ApplicationRoutes.Exercise.Connection.SetupInstructions,
                ApplicationRoutes.Goals.Overview,
                ApplicationRoutes.Goals.Compose,
                ApplicationRoutes.Achievements,
                ApplicationRoutes.Competitions.Overview,
                ApplicationRoutes.Debug,
                ApplicationRoutes.History -> {
                    val objectInstance = it.objectInstance!!
                    Button(onClick = { router.navTo(objectInstance) }) {
                        Text(text = +objectInstance.title)
                    }
                }
                null -> when (it) {
                    ApplicationRoutes.Exercise.ExerciseSelection::class -> {
                        val dest = ApplicationRoutes.Exercise.ExerciseSelection(DEMO.demoOptions)
                        Button(onClick = { router.navTo(dest) }) {
                            Text(text = +dest.title)
                        }
                    }
                    ApplicationRoutes.User.AccountManagement::class -> {
                        val dest = ApplicationRoutes.User.AccountManagement(Any())
                        Button(onClick = { router.navTo(dest) }) {
                            Text(text = +dest.title)
                        }
                    }
                    ApplicationRoutes.Exercise.Training::class -> {
                        val dest = ApplicationRoutes.Exercise.Training(
                            DEMO.demoOptions
                        )
                        Button(onClick = { router.navTo(dest) }) {
                            Text(text = +dest.title)
                        }
                    }
                    ApplicationRoutes.Exercise.ExerciseResults::class -> {
                        val dest = ApplicationRoutes.Exercise.ExerciseResults(DEMO.demoData)
                        Button(onClick = { router.navTo(dest) }) {
                            Text(text = +dest.title)
                        }
                    }
                    ApplicationRoutes.Exercise.Connection.KeyboardSynchronisation::class -> {
                        val dest = ApplicationRoutes.Exercise.Connection.KeyboardSynchronisation(DEMO.demoOptions)
                        Button(onClick = { router.navTo(dest) }) {
                            Text(text = +dest.title)
                        }
                    }
                    else -> {
                        Text(text = "Missing path in debug for class $it")
                    }
                }
            }
        }
    }
}

private fun <T : Any> getDebugPaths(kClass: KClass<T>): List<KClass<out T>> {
    return when {
        kClass.isSealed -> kClass.sealedSubclasses.map { getDebugPaths(it) }.flatten()
        kClass.isData || kClass.objectInstance != null -> {
            return listOf(kClass)
        }
        else -> emptyList()
    }

}

