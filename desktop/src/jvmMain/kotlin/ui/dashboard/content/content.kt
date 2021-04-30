@file:Suppress("FunctionName")

package ui.dashboard.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import com.github.jan222ik.common.ui.util.router.Router
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.SimpleYAxisDrawer
import ui.dashboard.ApplicationRoutes
import ui.dashboard.HoverIconDashboardCard
import ui.dashboard.StreakAPI
import ui.dashboard.goal_preview.DashboardGoalsPreviewCard
import ui.general.WindowRouterAmbient
import ui.util.i18n.LanguageAmbient
import ui.util.i18n.RequiresTranslationI18N
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

@Composable
fun DashboardContent() {
    val router = WindowRouterAmbient.current
    val density = LocalDensity.current
    Layout(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .padding(vertical = 48.dp),
        content = {
            DashStartBtnGroup(router)
            LastWeeksChart()
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
fun LastWeeksChart() {
    BaseDashboardCard {
        val langConfig = LanguageAmbient.current
        val lineChartData = remember(langConfig) {
            // TODO get data from database
            // TODO if there is a value below 20 set startAtZero to true otherwise keep false
            val translation3LetterDay = i18n.str.dashboard.weeklyChart.daysOfWeek.resolve().split(" ")
            LineChartData(
                points = listOf(
                    LineChartData.Point(84f, translation3LetterDay[5]),
                    LineChartData.Point(80f, translation3LetterDay[6]),
                    LineChartData.Point(90f, translation3LetterDay[0]),
                    LineChartData.Point(100f, translation3LetterDay[1]),
                    LineChartData.Point(0f, translation3LetterDay[2]),
                    LineChartData.Point(80f, translation3LetterDay[3]),
                    LineChartData.Point(123f, translation3LetterDay[4]),
                ),
                startAtZero = true
            )
        }
        Layout(
            content = {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = +i18n.str.dashboard.weeklyChart.chartsTitle,
                        style = MaterialTheme.typography.h5
                    )
                    TextButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = {
                            println("Pressed show more of charts")
                        }
                    ) {
                        Text(text = +i18n.str.dashboard.weeklyChart.showMore)
                    }
                }
                LineChart(
                    modifier = Modifier.padding(vertical = 16.dp),
                    lineChartData = lineChartData,
                    lineDrawer = SolidLineDrawer(
                        thickness = 5.dp,
                        color = MaterialTheme.colors.primary.copy(alpha = 0.6f)
                    ),
                    xAxisDrawer = SimpleXAxisDrawer(
                        labelTextSize = 16.sp,
                        labelTextColor = Color.White,
                        axisLineColor = Color.White,
                        axisLineThickness = 3.dp
                    ),
                    yAxisDrawer = SimpleYAxisDrawer(
                        labelTextSize = 16.sp,
                        labelTextColor = Color.White,
                        axisLineColor = Color.White,
                        axisLineThickness = 3.dp
                    ),
                    pointDrawer = FilledCircularPointDrawer(
                        color = MaterialTheme.colors.primary
                    )

                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        modifier = Modifier.padding(end = 5.dp).size(10.dp),
                        shape = CircleShape.copy(CornerSize(5.dp)),
                        color = MaterialTheme.colors.primary
                    ) {}
                    Text(+i18n.str.dashboard.weeklyChart.description)
                }
            }
        ) { measurables, constraints ->
            val pHeader = measurables[0].measure(Constraints(maxWidth = constraints.maxWidth))
            val pLegend = measurables[2].measure(Constraints(maxWidth = constraints.maxWidth))
            val pChart = measurables[1].measure(
                Constraints(
                    maxWidth = constraints.maxWidth,
                    maxHeight = constraints.maxHeight.minus(pHeader.height).minus(pLegend.height).coerceAtLeast(0)
                )
            )
            layout(
                width = constraints.maxWidth,
                height = constraints.maxHeight
            ) {
                pHeader.place(IntOffset.Zero)
                pChart.place(x = 0, y = pHeader.height)
                pLegend.place(x = 0, y = constraints.maxHeight - pLegend.height)
            }
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
                        Text(
                            text = streakapi.calcStreaks().toString() + " " + +RequiresTranslationI18N("Days"),
                            style = MaterialTheme.typography.h5
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.padding(paddingValues = PaddingValues(start = 10.dp)),
                            text = +RequiresTranslationI18N("Streak"),
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
                                contentDescription = "Open Camera Setup",
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
                                contentDescription = "Open AppBenefits",
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
