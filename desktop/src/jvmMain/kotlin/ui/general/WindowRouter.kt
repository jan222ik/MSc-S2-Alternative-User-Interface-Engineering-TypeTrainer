@file:Suppress("FunctionName")

package ui.general

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import com.github.jan222ik.common.ui.util.router.BaseRouter
import com.github.jan222ik.common.ui.util.router.Router
import ui.dashboard.ApplicationRoutes

val WindowRouterAmbient =
    compositionLocalOf<Router<ApplicationRoutes>> { error("No active router found!") }

@Composable
fun WindowRouter(
    initialRoute: ApplicationRoutes,
    routeDefinition: @Composable (last: ApplicationRoutes, router: Router<ApplicationRoutes>) -> Unit
) {
    BaseRouter(
        initialRoute = initialRoute,
        ambient = WindowRouterAmbient,
        routeDefinition = routeDefinition
    )
}
