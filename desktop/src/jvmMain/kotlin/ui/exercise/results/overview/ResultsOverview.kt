@file:Suppress("FunctionName")

package ui.exercise.results.overview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import ui.exercise.results.ResultIntent
import ui.util.i18n.i18n

@Composable
fun ColumnScope.ResultsOverview(intent: ResultIntent, isStandalone: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f.takeIf { isStandalone } ?: 1f)
            .fillMaxHeight()
            .align(Alignment.CenterHorizontally),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        KeyPoints(
            Modifier
                .fillMaxWidth(0.4f),
            keyPoints = intent.getKeyPoints()
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Goals(
                modifier = Modifier.fillMaxHeight(0.5f)
            )
            Achievements(
                modifier = Modifier.fillMaxHeight(),
                achievements = ResultIntent.getAchievements()
            )
        }
    }
}

@Composable
fun KeyPoints(modifier: Modifier, keyPoints: List<KeyPoint>) {
    BaseDashboardCard(
        modifier = modifier
    ) {
        Column {
            Text(text = +i18n.str.exercise.results.overview.keyPoints)
            Row {
                LazyColumn(
                    contentPadding = PaddingValues(end = 15.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    items(keyPoints) { keyPoint ->
                        Text(
                            text = keyPoint.name + ":",
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
                LazyColumn {
                    items(keyPoints) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = it.value,
                                textAlign = TextAlign.Justify
                            )
                            Icon(
                                imageVector = it.icon,
                                contentDescription = null,
                                tint = MaterialTheme.colors.primary,
                                modifier = Modifier.size(15.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun Goals(modifier: Modifier) {
    BaseDashboardCard(
        modifier = modifier
    ) {
        Text(text = +i18n.str.exercise.results.overview.goals)
    }
}

@Composable
fun Achievements(modifier: Modifier, achievements: List<List<String>>) {
    BaseDashboardCard(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Text(text = +i18n.str.exercise.results.overview.achievements)
                val color = MaterialTheme.colors.primary
                Text(
                    text = achievements.size.toString(),
                    modifier = Modifier.drawBehind {
                        drawCircle(color = color, radius = 13f)
                    }
                )

            }
            LazyColumn(
                contentPadding = PaddingValues(top = 5.dp),
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                items(achievements) { achievement ->
                    Card(
                        backgroundColor = MaterialTheme.colors.background,
                        border = BorderStroke(width = 2.dp, color = MaterialTheme.colors.primary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(0.85f).padding(top = 5.dp),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                        ) {
                            Text(
                                text = achievement.first(),
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Text(
                                text = achievement.last(),
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }
    }
}
