@file:Suppress("FunctionName")

package com.github.jan222ik.common.ui.util.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * The base router enables updating the UI by switching the current route.
 * The routes are defined as the generic T.
 *
 * @param initialRoute defines the initial route for the state
 * @param ambient defines the ProvidableCompositionLocal to make the router accessible to
 * @param routeDefinition Lambda with parameters of the current route and router for composable content
 */
@Composable
fun <T> BaseRouter(
    initialRoute: T,
    ambient: ProvidableCompositionLocal<Router<T>>,
    routeDefinition: @Composable (last: T, router: Router<T>) -> Unit
) {
    val router = RouterImpl(
        current = initialRoute
    )
    val (currentRouter, setCurrentRouter) = remember(initialRoute) { mutableStateOf(router as Router<T>) }
    router.setRouter = setCurrentRouter

    CompositionLocalProvider(
        ambient provides currentRouter
    ) {
        routeDefinition.invoke(currentRouter.current, currentRouter)
    }
}
