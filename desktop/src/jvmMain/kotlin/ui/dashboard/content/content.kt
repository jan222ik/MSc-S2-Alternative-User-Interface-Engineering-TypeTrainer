@file:Suppress("FunctionName")

package ui.dashboard.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material.icons.filled.Stars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import com.github.jan222ik.common.ui.util.router.Router
import ui.components.filling_layout_with_spacer.CustomLayoutHeightWithCenterSpace
import ui.dashboard.ApplicationRoutes
import ui.dashboard.HoverIconDashboardCard
import ui.exercise.TypingOptions
import ui.dashboard.BaseDashboardCard
import ui.dashboard.IconDashboardCard
import ui.dashboard.StreakAPI
import ui.general.WindowRouterAmbient
import ui.util.i18n.RequiresTranslationI18N
import ui.util.i18n.i18n
import ui.util.router.Router
import java.time.LocalDate

private const val iconFraction = .75f
private const val halfIconFraction = .5f
private val padding = 5.dp
private val size = 200.dp
private val halfSize = size.div(2).minus(padding.times(2))
private val standardCard = Modifier.size(size).padding(padding)
private val halfCard = Modifier.size(size, halfSize)
private val doubleCard = Modifier.size(size.times(2)).padding(padding)

/**
 * Content of the dashboard screen featuring two columns and two rows
 */
@Composable
fun DashboardContent() {
    val router = WindowRouterAmbient.current

    Row(
        modifier = Modifier.padding(all = padding)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(fraction = .5f)
        ) {
            LeftColumn(router = router)
        }
        Column {
            RightColumn(router = router)
        }
    }
}

/**
 * Left column of dashboard content
 */
@Composable
private fun LeftColumn(router: Router<ApplicationRoutes>) {
    Row(modifier = Modifier.height(size)) {
        HoverIconDashboardCard(
            modifier = standardCard,
            onClick = { router.navTo(ApplicationRoutes.Exercise.ExerciseSelection) },
            icon = {
                Icon(
                    modifier = Modifier.fillMaxSize(fraction = iconFraction),
                    imageVector = Icons.Filled.Keyboard,
                    contentDescription = "Start Practice",
                    tint = MaterialTheme.colors.onBackground
                )
            },
            text = {
                Text(+i18n.str.navigation.self.practice)
            }
        )
        HoverIconDashboardCard(
            modifier = standardCard,
            onClick = { router.navTo(ApplicationRoutes.Competitions.Overview) },
            icon = {
                Icon(
                    modifier = Modifier.fillMaxSize(fraction = iconFraction),
                    imageVector = Icons.Filled.Group,
                    contentDescription = "Start Competition",
                    tint = MaterialTheme.colors.onBackground
                )
            },
            text = {
                Text(+i18n.str.navigation.self.competition)
            }
        )
        CustomLayoutHeightWithCenterSpace(
            modifier = Modifier.padding(padding),
            centralPadding = padding.times(2),
            top = {
                HoverIconDashboardCard(
                    modifier = halfCard,
                    onClick = { router.navTo(ApplicationRoutes.History) },
                    icon = {
                        Icon(
                            modifier = Modifier.fillMaxSize(fraction = halfIconFraction),
                            imageVector = Icons.Filled.History,
                            contentDescription = "Open History",
                            tint = MaterialTheme.colors.onBackground
                        )
                    },
                    text = {
                        Text(+i18n.str.navigation.self.history)
                    }
                )
            },
            bottom = {
                HoverIconDashboardCard(
                    modifier = halfCard,
                    onClick = { router.navTo(ApplicationRoutes.Achievements) },
                    icon = {
                        Icon(
                            modifier = Modifier.fillMaxSize(fraction = halfIconFraction),
                            imageVector = Icons.Filled.Stars,
                            contentDescription = "Open Achievements",
                            tint = MaterialTheme.colors.onBackground
                        )
                    },
                    text = {
                        Text(+i18n.str.navigation.self.achievements)
                    }
                )
            }
        )
    }
    Row(modifier = Modifier.padding(all = padding)) {
        BaseDashboardCard {
            Text("Last Week's Statistics Placeholder")
        }
    }
}

/**
 * Right column of dashboard screen
 */
@Composable
private fun RightColumn(router: Router<ApplicationRoutes>) {
    Row(modifier = Modifier.height(size).padding(all = padding)) {
        BaseDashboardCard {
            Text("Goals Placeholder")
        }
    }
    Row(modifier = Modifier.padding(all = padding)) {
        BaseDashboardCard(
            modifier = doubleCard
        ) {
            val streakapi = StreakAPI()
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        modifier = Modifier.size(64.dp),
                        imageVector = Icons.Filled.Whatshot,
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                    Text(text = streakapi.calcStreaks().toString() + " " + +RequiresTranslationI18N("Days"))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(paddingValues = PaddingValues(start = 10.dp)),
                        text = +RequiresTranslationI18N("Streak")
                    )
                    Text(text = +RequiresTranslationI18N(LocalDate.now().month.name) + ", " + LocalDate.now().year)
                }

            }
        }
        CustomLayoutHeightWithCenterSpace(
            centralPadding = padding.div(2),
            top = {
                HoverIconDashboardCard(
                    modifier = standardCard,
                    onClick = { router.navTo(ApplicationRoutes.Exercise.Connection.SetupInstructions(null)) },
                    icon = {
                        Icon(
                            modifier = Modifier.fillMaxSize(fraction = iconFraction),
                            imageVector = Icons.Filled.PhotoCamera,
                            contentDescription = "Open Camera Setup",
                            tint = MaterialTheme.colors.onBackground
                        )
                    },
                    text = {
                        Text(+i18n.str.navigation.self.camera_setup)
                    }
                )
            },
            bottom = {
                HoverIconDashboardCard(
                    modifier = standardCard,
                    onClick = { router.navTo(ApplicationRoutes.AppBenefits) },
                    icon = {
                        Icon(
                            modifier = Modifier.fillMaxSize(fraction = iconFraction),
                            imageVector = Icons.Filled.SentimentVerySatisfied,
                            contentDescription = "Open AppBenefits",
                            tint = MaterialTheme.colors.onBackground
                        )
                    },
                    text = {
                        Text(+i18n.str.navigation.self.app_benefits)
                    }
                )
            }
        )
    }
}
