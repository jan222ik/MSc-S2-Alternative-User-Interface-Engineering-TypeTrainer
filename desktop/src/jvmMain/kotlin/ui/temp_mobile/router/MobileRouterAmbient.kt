@file:Suppress("FunctionName")

package ui.temp_mobile.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import ui.util.router.BaseRouter
import ui.util.router.Router

val MobileRouterAmbient = compositionLocalOf<Router<MobileRoutes>> { error("No active router found!") }

@Composable
fun MobileRouter(
    initialRoute: MobileRoutes,
    routeDefinition: @Composable (last: MobileRoutes, router: Router<MobileRoutes>) -> Unit
) {
    BaseRouter(
        initialRoute = initialRoute,
        ambient = MobileRouterAmbient,
        routeDefinition = routeDefinition
    )
}
