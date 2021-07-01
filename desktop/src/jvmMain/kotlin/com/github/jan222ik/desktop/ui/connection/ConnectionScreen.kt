@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.connection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import com.github.jan222ik.desktop.network.NDSServer
import com.github.jan222ik.desktop.network.NDSState
import com.github.jan222ik.desktop.network.Server
import com.github.jan222ik.desktop.ui.dashboard.ApplicationRoutes
import com.github.jan222ik.desktop.ui.exercise.AbstractTypingOptions
import com.github.jan222ik.desktop.ui.general.WindowRouterAmbient
import com.github.jan222ik.desktop.ui.util.i18n.i18n

@HasDoc
@Composable
fun ConnectionScreen(server: Server, trainingOptions: AbstractTypingOptions) {
    val hasConnection = server.connectionStatus.collectAsState()
    val router = WindowRouterAmbient.current

    when (hasConnection.value) {
        true -> {
            router.navTo(dest = ApplicationRoutes.Exercise.Connection.KeyboardSynchronisation(trainingOptions))
        }
        false -> {
            val ndsStateFlow = remember { MutableStateFlow(NDSState.STARTING) }
            val nds = ndsStateFlow.collectAsState()

            NDSServer.coroutineScope = rememberCoroutineScope()
            rememberCoroutineScope().launch {
                NDSServer.start(server)
                ndsStateFlow.emit(NDSState.STARTED)
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                BaseDashboardCard(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(vertical = 16.dp)
                ) {
                    Column {
                        when (nds.value) {
                            NDSState.STARTING -> Text(+i18n.str.exercise.connection.ndsStarting)
                            NDSState.STARTED -> Text(+i18n.str.exercise.connection.ndsStarting)
                        }

                        TextButton(
                            onClick = {
                                NDSServer.stop()
                                router.navTo(
                                    ApplicationRoutes.Exercise.Training(
                                        trainingOptions = trainingOptions.copyOptions(
                                            isCameraEnabled = false
                                        )
                                    )
                                )
                            }
                        ) {
                            Text(+i18n.str.exercise.connection.continueWithoutHandtracking)
                        }
                    }
                }
            }
        }
    }
}

