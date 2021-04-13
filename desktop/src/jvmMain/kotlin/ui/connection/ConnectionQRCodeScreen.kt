@file:Suppress("FunctionName")

package ui.connection

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import network.Server
import ui.components.qr.QRCode
import ui.dashboard.ApplicationRoutes
import ui.exercise.ITypingOptions
import ui.general.WindowRouterAmbient
import ui.util.i18n.RequiresTranslationI18N

@Composable
fun ConnectionQRCodeScreen(server: Server, trainingOptions: ITypingOptions) {
    val hasConnection = server.connectionStatus.collectAsState()
    val router = WindowRouterAmbient.current
    when (hasConnection.value) {
        true -> {
            router.navTo(ApplicationRoutes.Exercise.Connection.SetupInstructions(trainingOptions = trainingOptions))
        }
        false -> {
            BaseDashboardCard {
                Column {
                    Text(+RequiresTranslationI18N("Scan to connect!"))
                    QRCode()
                    TextButton(
                        onClick = {
                            router.navTo(ApplicationRoutes.Exercise.Training(trainingOptions = trainingOptions.copyOptions(isCameraEnabled = false)))
                        }
                    ) {
                        Text(+RequiresTranslationI18N("Or continue without hand tracking"))
                    }
                }
            }
        }
    }
}
