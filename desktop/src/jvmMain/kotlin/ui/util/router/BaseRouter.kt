package ui.util.router

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

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
