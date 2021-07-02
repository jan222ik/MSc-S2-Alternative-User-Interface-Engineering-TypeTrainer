@file:Suppress("FunctionName")

package com.github.jan222ik.common.ui.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.common.ui.util.router.BaseRouter
import com.github.jan222ik.common.ui.util.router.Router

val MobileRouterAmbient =
    compositionLocalOf<Router<MobileRoutes>> { error("No active router found!") }

@HasDoc
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
