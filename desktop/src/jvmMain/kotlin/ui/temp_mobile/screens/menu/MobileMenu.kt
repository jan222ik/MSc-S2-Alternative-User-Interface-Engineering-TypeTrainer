@file:Suppress("FunctionName")

package ui.temp_mobile.screens.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Phonelink
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.components.Logo
import ui.dashboard.BaseDashboardCard
import ui.dashboard.content.IconDashboardCard
import ui.temp_mobile.router.MobileRouterAmbient
import ui.temp_mobile.router.MobileRoutes
import kotlin.math.max
import kotlin.math.min

@ExperimentalLayout
@Composable
fun MobileMenu() {
    val router = MobileRouterAmbient.current
    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            Wave(cornerSize = 32.dp)
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Logo(modifier = Modifier.height(100.dp))
                Text(
                    text = "TypeTrainer",
                    style = MaterialTheme.typography.h3
                )
            }
        }
        Card(
            modifier = Modifier.fillMaxHeight(),
            shape = RoundedCornerShape(topEnd = 32.dp),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .preferredHeight(IntrinsicSize.Max),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                BaseDashboardCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .preferredHeight(IntrinsicSize.Min)
                ) {
                    Spacer(Modifier.height(200.dp))
                    Text(text = "Weeks Stats")
                }
                BaseDashboardCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .preferredHeight(IntrinsicSize.Min)
                ) {
                    Row {
                        Text(
                            modifier = Modifier.fillMaxSize(0.5f),
                            text = "Streak"
                        )
                        Text(text = "Total Exercises")
                    }
                }
                Row {
                    val interPaddingHalf = 16.dp.div(2)
                    Square(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(end = interPaddingHalf)
                    ) {
                        IconDashboardCard(
                            modifier = Modifier.fillMaxSize(),
                            onClick = {},
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.CameraAlt,
                                    contentDescription = null
                                )
                            },
                            text = {
                                Text("Cam Setup")
                            }
                        )
                    }
                    Square(
                        modifier = Modifier
                            .padding(start = interPaddingHalf)
                            .fillMaxWidth()
                    ) {
                        BaseDashboardCard(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text("App benefits")
                        }
                    }
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    EFabWithBackground(
                        onClick = {
                            router.navTo(MobileRoutes.Scanner)
                        }
                    ) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .fillMaxWidth(0.85f)
                                .height(42.dp),
                            shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp),
                            color = MaterialTheme.colors.background
                        ) {}
                    }
                }
            }
        }
    }
}

@Composable
fun Square(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(
        modifier = modifier,
        content = content
    ) { list, constraints ->
        require(list.size == 1) { "Square composable may only have one direct child" }
        val squareSize = min(constraints.maxWidth, constraints.maxHeight)
        val placeable =
            list.first().measure(constraints.copy(maxWidth = squareSize, maxHeight = squareSize))
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}

@Composable
fun BoxScope.Wave(cornerSize: Dp) {
    val modifier = Modifier
        .align(Alignment.BottomStart)
        .width(cornerSize)
        .height(cornerSize.times(2))
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.surface,
    ) { }
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.background,
        shape = RoundedCornerShape(bottomStart = cornerSize)
    ) { }
}

@Composable
fun EFabWithBackground(onClick: () -> Unit, backgroundShape: @Composable() () -> Unit) {
    Layout(content = {
        backgroundShape.invoke()
        ExtendedFloatingActionButton(
            backgroundColor = MaterialTheme.colors.primary,
            onClick = onClick,
            text = {
                Text("Connect to PC")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Phonelink,
                    contentDescription = null
                )
            }
        )
    }) { list, constraints ->
        val shape = list[0].measure(constraints)
        val efab = list[1].measure(constraints)
        val height = shape.height + efab.height / 2
        val maxWidth = max(shape.width, efab.width)
        layout(width = maxWidth, height = height) {
            shape.placeRelative(0, y = efab.height / 2)
            efab.placeRelative(x = (maxWidth - efab.width) / 2, y = 0)
        }
    }
}



