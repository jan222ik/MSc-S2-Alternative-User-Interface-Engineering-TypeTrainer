@file:Suppress("FunctionName")

package ui.dashboard.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import com.github.jan222ik.common.ui.util.router.Router
import kotlinx.coroutines.GlobalScope
import ui.dashboard.ApplicationRoutes
import ui.dashboard.HoverIconDashboardCard
import ui.dashboard.StreakAPI
import ui.dashboard.goal_preview.DashboardGoalsPreviewCard
import ui.general.WindowRouterAmbient
import ui.util.i18n.LanguageAmbient
import ui.util.i18n.LanguageDefinition
import ui.util.i18n.i18n
import java.time.LocalDate
import java.time.format.TextStyle
import kotlin.math.roundToInt

private const val iconFraction = .75f
private const val halfIconFraction = .5f


@Composable
fun DashStartBtnGroup(router: Router<ApplicationRoutes>) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val hPadding = 16.dp
        val vPadding = 16.dp
        val btnWidth = this.maxWidth.minus(hPadding.times(2)).div(3)
        Row(
            horizontalArrangement = Arrangement.spacedBy(hPadding)
        ) {
            HoverIconDashboardCard(
                modifier = Modifier.width(btnWidth).heightIn(max = this@BoxWithConstraints.maxHeight),
                onClick = { router.navTo(ApplicationRoutes.Exercise.ExerciseSelection(null)) },
                icon = {
                    Icon(
                        modifier = Modifier.fillMaxSize(fraction = iconFraction),
                        imageVector = Icons.Filled.Keyboard,
                        contentDescription = "Start Practice",
                        tint = MaterialTheme.colors.onBackground
                    )
                },
                text = {
                    Text(+i18n.str.navigation.self.practice, style = MaterialTheme.typography.h4)
                }
            )
            HoverIconDashboardCard(
                modifier = Modifier.width(btnWidth).heightIn(max = this@BoxWithConstraints.maxHeight),
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
                    Text(+i18n.str.navigation.self.competition, style = MaterialTheme.typography.h4)
                }
            )
            Column(
                modifier = Modifier.width(btnWidth).height(this@BoxWithConstraints.maxHeight),
                verticalArrangement = Arrangement.spacedBy(vPadding)
            ) {
                val height = this@BoxWithConstraints.maxHeight.minus(vPadding).div(2)
                HoverIconDashboardCard(
                    modifier = Modifier.height(height),
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
                        Text(+i18n.str.navigation.self.history, style = MaterialTheme.typography.h5)
                    }
                )
                HoverIconDashboardCard(
                    modifier = Modifier.height(height),
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
                        Text(+i18n.str.navigation.self.achievements, style = MaterialTheme.typography.h5)
                    }
                )
            }
        }
    }
}

@HasDoc
@Composable
fun DashboardContent(
    initStatsIntent: DashboardStatsIntent? = null
) {
    val statsIntent =
        remember(initStatsIntent) { initStatsIntent ?: DashboardStatsIntent().apply { init(GlobalScope) } }
    val router = WindowRouterAmbient.current
    val density = LocalDensity.current
    Layout(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 48.dp),
        content = {
            DashStartBtnGroup(router)
            LastWeeksChart(statsIntent)
            DashboardGoalsPreviewCard()
            StreakAndBtns(router)
        }
    ) { measurables, constraints ->
        val hPadding = with(density) { 8.dp.toPx() }.roundToInt()
        val vPadding = with(density) { 16.dp.toPx() }.roundToInt()
        val leftWidth = constraints.maxWidth.times(0.5f).roundToInt()
        val rightWidth = constraints.maxWidth.minus(leftWidth)
        val topHeight = constraints.maxHeight.times(0.35f).roundToInt()
        val bottomHeight = constraints.maxHeight.minus(topHeight)
        val pBtnGroup =
            measurables[0].measure(Constraints(maxWidth = leftWidth - hPadding, maxHeight = topHeight - vPadding))
        val chart =
            measurables[1].measure(Constraints(maxWidth = leftWidth - hPadding, maxHeight = bottomHeight - vPadding))
        val goals =
            measurables[2].measure(Constraints(maxWidth = rightWidth - hPadding, maxHeight = topHeight - vPadding))
        val streakAndBtns =
            measurables[3].measure(Constraints(maxWidth = rightWidth - hPadding, maxHeight = bottomHeight - vPadding))
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {
            pBtnGroup.place(IntOffset.Zero)
            chart.place(x = 0, y = topHeight + vPadding)
            goals.place(x = leftWidth + hPadding, y = 0)
            streakAndBtns.place(x = leftWidth + hPadding, y = topHeight + vPadding)
        }
    }
}


@Composable
fun StreakAndBtns(router: Router<ApplicationRoutes>) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val hPadding = 16.dp
        val vPadding = 16.dp
        val streakWidth = this.maxWidth.minus(hPadding).times(0.6f)
        val btnsWidth = this.maxWidth.minus(hPadding).times(0.4f)
        Row(horizontalArrangement = Arrangement.spacedBy(hPadding)) {
            BaseDashboardCard(
                modifier = Modifier.width(streakWidth).height(this@BoxWithConstraints.maxHeight)
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
                        val calcStreaks = streakapi.calcStreaks()
                        val lang = LanguageAmbient.current
                        Text(
                            text = "$calcStreaks " + i18n.str.dashboard.streak.days.observedString()
                                .let {
                                    val singleDayLastLetterTrunc =
                                        lang.language == LanguageDefinition.English ||
                                                lang.language == LanguageDefinition.German
                                    if (calcStreaks == 1 && singleDayLastLetterTrunc) {
                                        // Truncate last letter if value is 1 "Tage" -> "Tag"; "Days" -> "Day"
                                        it.substring(0, it.length.dec())
                                    } else it
                                },
                            style = MaterialTheme.typography.h5
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.padding(paddingValues = PaddingValues(start = 10.dp)),
                            text = +i18n.str.dashboard.streak.streakTitle,
                            style = MaterialTheme.typography.h6
                        )
                        Text(
                            modifier = Modifier.padding(end = 27.dp),
                            text = LocalDate.now().month
                                .getDisplayName(TextStyle.FULL, LanguageAmbient.current.language.locale)
                                    + ", " + LocalDate.now().year,
                            style = MaterialTheme.typography.h6
                        )
                    }
                    Spacer(modifier = Modifier.size(20.dp))
                    StreakCalendar()
                }
            }
            BoxWithConstraints(
                modifier = Modifier.width(btnsWidth)
            ) {
                val height = this.maxHeight.minus(vPadding).div(2)
                Column(
                    verticalArrangement = Arrangement.spacedBy(vPadding)
                ) {
                    HoverIconDashboardCard(
                        modifier = Modifier.height(height),
                        onClick = { router.navTo(ApplicationRoutes.Exercise.Connection.SetupInstructions(null)) },
                        icon = {
                            Icon(
                                modifier = Modifier.fillMaxSize(fraction = iconFraction),
                                imageVector = Icons.Filled.PhotoCamera,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onBackground
                            )
                        },
                        text = {
                            Text(+i18n.str.navigation.self.camera_setup, style = MaterialTheme.typography.h5)
                        }
                    )
                    HoverIconDashboardCard(
                        modifier = Modifier.height(height),
                        onClick = { router.navTo(ApplicationRoutes.AppBenefits) },
                        icon = {
                            Icon(
                                modifier = Modifier.fillMaxSize(fraction = iconFraction),
                                imageVector = Icons.Filled.SentimentVerySatisfied,
                                contentDescription = null,
                                tint = MaterialTheme.colors.onBackground
                            )
                        },
                        text = {
                            Text(+i18n.str.navigation.self.app_benefits, style = MaterialTheme.typography.h5)
                        }
                    )
                }
            }
        }
    }
}
