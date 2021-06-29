@file:Suppress("FunctionName")

package com.github.jan222ik.common.ui

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Phonelink
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.common.dto.MobileStatsData
import com.github.jan222ik.common.dto.SHARED_STATS_PREF_KEY
import com.github.jan222ik.common.ui.components.Logo
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import com.github.jan222ik.common.ui.dashboard.IconDashboardCard
import com.github.jan222ik.common.ui.router.MobileRouterAmbient
import com.github.jan222ik.common.ui.router.MobileRoutes
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlin.math.max
import kotlin.math.min

@HasDoc
@Composable
fun MobileMenu(activity: Activity, lostConnection: Boolean = false) {
    val data: MobileStatsData? = activity
        .getPreferences(Context.MODE_PRIVATE)
        .getString(SHARED_STATS_PREF_KEY, null)
        ?.let {
            try {
                Json.decodeFromString<MobileStatsData>(it)
            } catch (ex: Exception) {
                null
            }
        }
    val router = MobileRouterAmbient.current
    var lostConnection = lostConnection
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
            Scaffold(
                backgroundColor = MaterialTheme.colors.surface,
                content = { paddingValues ->
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp, bottom = 30.dp.plus(paddingValues.calculateBottomPadding()))
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        BaseDashboardCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            Spacer(Modifier.height(200.dp))
                            Text(text = "Weeks Stats")
                        }
                        BaseDashboardCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {

                            Row {
                                @Composable
                                fun item(
                                    widthFraction: Float,
                                    imageVector: ImageVector,
                                    text: String,
                                    title: String
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(widthFraction)
                                            .fillMaxHeight()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .align(Alignment.TopCenter)
                                                .fillMaxHeight()
                                                .offset(y = (-10).dp, x = (-10).dp),
                                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                modifier = Modifier.fillMaxHeight(0.75f).fillMaxWidth(0.5f),
                                                imageVector = imageVector,
                                                contentDescription = null
                                            )
                                            Text(
                                                text = text
                                            )
                                        }
                                        Text(
                                            modifier = Modifier.align(Alignment.BottomCenter),
                                            text = title
                                        )
                                    }
                                }

                                item(
                                    title = "Streak",
                                    imageVector = Icons.Filled.Whatshot,
                                    text = "${data?.streak ?: "?"} Days",
                                    widthFraction = 0.5f
                                )
                                item(
                                    title = "Total Exercises",
                                    imageVector = Icons.Filled.Functions,
                                    text = data?.totalExercises?.toString() ?: "?",
                                    widthFraction = 1f
                                )

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
                                    onClick = {
                                        router.navTo(MobileRoutes.CameraSetup)
                                    },
                                    icon = {
                                        Icon(
                                            modifier = Modifier.align(Alignment.Center).fillMaxSize(0.75f),
                                            imageVector = Icons.Filled.CameraAlt,
                                            contentDescription = null
                                        )
                                    },
                                    text = {
                                        Text("Camera Setup")
                                    }
                                )
                            }
                            Square(
                                modifier = Modifier
                                    .padding(start = interPaddingHalf)
                                    .fillMaxWidth()
                            ) {
                                IconDashboardCard(
                                    modifier = Modifier.fillMaxSize(),
                                    onClick = {
                                        router.navTo(MobileRoutes.AppBenefits)
                                    },
                                    icon = {
                                        Icon(
                                            modifier = Modifier.align(Alignment.Center).fillMaxSize(0.75f),
                                            imageVector = Icons.Filled.SentimentSatisfied,
                                            contentDescription = null
                                        )
                                    },
                                    text = {
                                        Text("App benefits")
                                    }
                                )
                            }
                        }
                    }
                },
                bottomBar = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
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
            )
        }
    }

    if (lostConnection) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = "Error") },
            text = { Text("Connection to desktop application lost") },
            confirmButton = {},
            dismissButton = {
                Button(
                    onClick = {
                        lostConnection = false
                    }) {
                    Text("This is the dismiss Button")
                }
            }
        )
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
fun EFabWithBackground(onClick: () -> Unit, backgroundShape: @Composable () -> Unit) {
    Layout(content = {
        backgroundShape.invoke()
        ExtendedFloatingActionButton(
            backgroundColor = MaterialTheme.colors.primary,
            onClick = onClick,
            text = {
                Text("Connect to PC", style = MaterialTheme.typography.h5)
            },
            icon = {
                Icon(
                    modifier = Modifier,
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




