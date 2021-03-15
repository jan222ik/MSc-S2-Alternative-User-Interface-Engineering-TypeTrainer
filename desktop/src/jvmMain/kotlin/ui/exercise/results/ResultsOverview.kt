@file:Suppress("FunctionName")

package ui.exercise.results

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.dashboard.BaseDashboardCard
import ui.util.i18n.RequiresTranslationI18N

@Composable
fun ColumnScope.ResultsOverview() {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .fillMaxHeight()
            .align(Alignment.CenterHorizontally),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        KeyPoints(
            Modifier
                .fillMaxWidth(0.4f),
            keyPoints = ResultIntent.getKeyPoints()
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
            Text(text = +RequiresTranslationI18N("KeyPoints") + ":")
            LazyColumn {

                items(keyPoints) { keyPoint ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = keyPoint.name + ":",
                            color = MaterialTheme.colors.primary,
                            modifier = Modifier.weight(1f)
                        )
                        Row(modifier = Modifier.weight(1f)) {
                            Text(text = keyPoint.value)
                            Icon(
                                imageVector = keyPoint.icon,
                                contentDescription = "Icon",
                                tint = MaterialTheme.colors.primary
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
        Text(text = +RequiresTranslationI18N("Goals") + ":")
    }
}

@Composable
fun Achievements(modifier: Modifier, achievements: List<List<String>>) {
    BaseDashboardCard(
        modifier = modifier
    ) {
        Column {
            Text(text = +RequiresTranslationI18N("Achievements") + ":")
            LazyColumn {
                items(achievements) { achievement ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = achievement.first(),
                            fontStyle = FontStyle.Italic,
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
