@file:Suppress("FunctionName")

package ui.dashboard.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import ui.dashboard.ApplicationRoutes
import ui.dashboard.BaseDashboardCard
import ui.general.WindowRouterAmbient
import ui.util.i18n.LanguageAmbient
import ui.util.i18n.LanguageDefinition
import ui.util.i18n.i18n
import kotlin.math.max

@ExperimentalLayout
@Composable
fun DashboardContent() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        val modIntrinsicMin = Modifier
            .padding(horizontal = 4.dp)
            .preferredWidth(IntrinsicSize.Min)
            .preferredHeight(IntrinsicSize.Min)
        TopRow(
            rowItemModifier = modIntrinsicMin
        )
    }
}

@ExperimentalLayout
@Composable
private fun TopRow(rowItemModifier: Modifier) {
    val router = WindowRouterAmbient.current
    Row {
        val iconCardModifier = Modifier
            .padding(horizontal = 4.dp)
            .size(150.dp)
        IconDashboardCard(
            modifier = iconCardModifier,
            onClick = {
                router.navTo(ApplicationRoutes.Exercise.ExerciseSelection)
            },
            icon = {
                Icon(
                    modifier = Modifier.fillMaxSize(0.75f),
                    imageVector = Icons.Filled.Keyboard,
                    contentDescription = "Start Practice",
                    tint = MaterialTheme.colors.onBackground
                )
            },
            text = {
                Text("Start Practice")
            }
        )
        IconDashboardCard(
            modifier = iconCardModifier,
            onClick = {
                router.navTo(ApplicationRoutes.Settings)
            },
            icon = {
                Icon(
                    modifier = Modifier.fillMaxSize(0.75f),
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Open Settings",
                    tint = MaterialTheme.colors.onBackground
                )
            },
            text = {
                Text(+i18n.str.navigation.self.settings)
            }
        )
        IconDashboardCard(
            modifier = iconCardModifier,
            onClick = {
                router.navTo(ApplicationRoutes.History)
            },
            icon = {
                Icon(
                    modifier = Modifier.fillMaxSize(0.75f),
                    imageVector = Icons.Filled.History,
                    contentDescription = "Open History",
                    tint = MaterialTheme.colors.onBackground
                )
            },
            text = {
                Text("History")
            }
        )
        val currentLang = LanguageAmbient.current
        BaseDashboardCard(
            modifier = rowItemModifier.clickable {
                val language = LanguageDefinition.German
                    .takeIf { currentLang.language != LanguageDefinition.German } ?: LanguageDefinition.English
                currentLang.changeLanguage(language)
            }
        ) {
            Text(text = "Change Language to " + ("German".takeUnless { currentLang.language == LanguageDefinition.German }
                ?: "English"))
        }

        BaseDashboardCard(
            modifier = rowItemModifier.clickable {
                router.navTo(ApplicationRoutes.Goals.Overview)
            }
        ) {
            Text(text = "Open Goals")
        }

        BaseDashboardCard(
            modifier = rowItemModifier
        ) {
            Text(text = "Statistics")
        }

        BaseDashboardCard(
            modifier = rowItemModifier
        ) {
            Text(text = "My Locations")
        }

        BaseDashboardCard(
            modifier = rowItemModifier
        ) {
            Text(text = "Pictures")
        }

        BaseDashboardCard(
            modifier = rowItemModifier.clickable {
                router.navTo(ApplicationRoutes.Debug)
            }
        ) {
            Text(text = "Debug")
        }
    }
}


/**
 * Specialized clickable BaseDashboardCard with designated text and icon slot.
 *
 * @param modifier Modifier for BaseDashboardCard
 * @param onClick invoked on click
 * @param text lambda for composable content defining the text
 * @param icon lambda for composable content defining the icon
 */
@ExperimentalLayout
@Composable
fun IconDashboardCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: @Composable BoxScope.() -> Unit,
    icon: @Composable BoxScope.() -> Unit
) {
    BaseDashboardCard(
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = modifier
                .padding(all = 16.dp)
                .fillMaxSize(),
        ) {
            Layout(
                content = {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        text.invoke(this)
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        icon.invoke(this)
                    }
                }
            ) { measurables, constraints ->
                val placeable1 = measurables[1].measure(constraints)
                val heightConstraint = constraints.copy(maxHeight = constraints.maxHeight - placeable1.height)
                val placeable2 = measurables[0].measure(heightConstraint)
                layout(
                    width = max(max(placeable1.width, placeable2.width), 0),
                    height = placeable1.height + placeable2.height
                ) {
                    placeable1.placeRelative(x = 0, y = 0)
                    placeable2.placeRelative(x = 0, y = placeable1.height)
                }
            }
        }
    }
}
