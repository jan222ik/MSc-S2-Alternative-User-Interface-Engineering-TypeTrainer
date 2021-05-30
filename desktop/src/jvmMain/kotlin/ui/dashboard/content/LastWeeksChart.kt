@file:Suppress("FunctionName", "DuplicatedCode")

package ui.dashboard.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import com.github.tehras.charts.line.renderer.line.SolidLineDrawer
import com.github.tehras.charts.line.renderer.point.FilledCircularPointDrawer
import com.github.tehras.charts.line.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.line.renderer.yaxis.SimpleYAxisDrawer
import ui.util.i18n.LanguageAmbient
import ui.util.i18n.i18n

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
