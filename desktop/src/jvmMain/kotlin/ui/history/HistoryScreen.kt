@file:Suppress("FunctionName")

package ui.history

import androidx.compose.desktop.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import org.jetbrains.exposed.sql.transactions.transaction
import textgen.database.DbHistoryDAO
import ui.exercise.results.ResultsRoutes
import ui.exercise.results.ResultsScreen
import ui.util.debug.ifDebugCompose
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

@ExperimentalFoundationApi
@Composable
fun HistoryScreen() {
    val items = remember {
        transaction {
            DbHistoryDAO
                .all()
                .map(DbHistoryDAO::toModel)
                .sortedByDescending { it.timestamp }
        }
    }
    val currItem = remember(items) { mutableStateOf(0) }
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                // .fillMaxWidth(0.3f)
                .padding(horizontal = 8.dp)
                .padding(vertical = 16.dp)
                .fillMaxHeight()
        ) {
            if (items.isNotEmpty()) {
                val local = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS)
                var today = true
                var yesterday = true
                var thisWeek = true
                var lastWeek = true
                var lastMonth = true
                var older = true
                items.forEachIndexed { index, item ->
                    val date = item.timestamp
                    val untilToday =
                        date.truncatedTo(ChronoUnit.DAYS).until(local, ChronoUnit.DAYS).toInt().absoluteValue
                    println("date: $date   - $untilToday")
                    when {
                        untilToday == 0 && today -> {
                            today = false
                            "Today:"
                        }
                        untilToday == 1 && yesterday -> {
                            yesterday = false
                            "Yesterday:"
                        }
                        (2 until 7).contains(untilToday) && thisWeek -> {
                            thisWeek = false
                            "This Week:"
                        }
                        (7 until 14).contains(untilToday) && lastWeek -> {
                            lastWeek = false
                            "Last Week:"
                        }
                        (14 until 31).contains(untilToday) && lastMonth -> {
                            lastMonth = false
                            "During Last Month:"
                        }
                        untilToday > 31 && older -> {
                            older = false
                            "Older:"
                        }
                        else -> null
                    }?.let {
                        stickyHeader {
                            Text(text = it)
                        }
                    }
                    item {
                        HistoryEntry(
                            index = index,
                            dateTime = date,
                            isCurrent = index == currItem.value,
                            onClick = {
                                currItem.value = index
                            }
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (items.isNotEmpty()) {
                val item = remember(currItem.value, items) { items[currItem.value] }
                ifDebugCompose {
                    Window {
                        BaseDashboardCard {
                            Column(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                            ) {
                                Text("histId:" + item.histId.value)
                                Text("timestamp:" + item.timestamp)
                                Text("dataJson:" + item.dataJson)
                                Text("data:" + item.data.value)
                            }
                        }
                    }
                }
                ResultsScreen(
                    exerciseEvaluation = item.data.value,
                    innerRouting = ResultsRoutes.OVERVIEW,
                    isStandalone = false
                )
            } else {
                Text("Complete a practice to see your results here!")
            }
        }
    }
}

@Composable
fun HistoryEntry(index: Int, dateTime: LocalDateTime, isCurrent: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable(onClick = onClick),
        backgroundColor = when (isCurrent) {
            true -> MaterialTheme.colors.primary
            false -> MaterialTheme.colors.surface
        }
    ) {
        Row {
            Text(text = index.inc().toString()) // Shift visual index by +1
            Column {
                val date = dateTime.format(DateTimeFormatter.ofPattern("dd-MM-yy"))
                val time = dateTime.format(DateTimeFormatter.ofPattern("hh:mm"))
                Text(text = "$date at $time")
            }
        }
    }
}

