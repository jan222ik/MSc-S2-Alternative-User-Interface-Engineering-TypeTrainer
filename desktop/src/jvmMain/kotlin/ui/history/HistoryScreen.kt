@file:Suppress("FunctionName")

package ui.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Today
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.exposed.sql.transactions.transaction
import textgen.database.DbHistoryDAO
import ui.exercise.results.ResultsRoutes
import ui.exercise.results.ResultsScreen
import ui.util.i18n.i18n
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
                .fillMaxWidth(0.1f)
                .padding(start = 16.dp)
                .padding(vertical = 16.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(5.dp)
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
                            i18n.str.history.today
                        }
                        untilToday == 1 && yesterday -> {
                            yesterday = false
                            i18n.str.history.yesterday
                        }
                        (2 until 7).contains(untilToday) && thisWeek -> {
                            thisWeek = false
                            i18n.str.history.thisWeek
                        }
                        (7 until 14).contains(untilToday) && lastWeek -> {
                            lastWeek = false
                            i18n.str.history.lastWeek
                        }
                        (14 until 31).contains(untilToday) && lastMonth -> {
                            lastMonth = false
                            i18n.str.history.lastMonth
                        }
                        untilToday > 31 && older -> {
                            older = false
                            i18n.str.history.older
                        }
                        else -> null
                    }?.let {
                        stickyHeader {
                            Surface(
                                modifier = Modifier.fillParentMaxWidth(),
                                color = MaterialTheme.colors.surface,
                                elevation = 0.dp
                            ) {
                                Text(text = +it)
                            }
                        }
                    }
                    item {
                        HistoryEntry(
                            index = items.size - index,
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
                /*
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
                 */
                Box(Modifier.padding(horizontal = 16.dp)) {
                    ResultsScreen(
                        exerciseEvaluation = item.data.value,
                        innerRouting = ResultsRoutes.OVERVIEW,
                        isStandalone = false
                    )
                }
            } else {
                Text("Complete a practice to see your results here!")
            }
        }
    }
}

@Composable
fun LazyItemScope.HistoryEntry(index: Int, dateTime: LocalDateTime, isCurrent: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillParentMaxWidth()
            .clickable(onClick = onClick),
        backgroundColor = when (isCurrent) {
            true -> MaterialTheme.colors.primary
            false -> MaterialTheme.colors.surface
        }
    ) {
        Row(
            modifier = Modifier.padding(all = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .fillParentMaxWidth(0.3f)
                    .padding(horizontal = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(modifier = Modifier.fillMaxHeight(), text = index.toString())
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp))
            {
                val date = dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yy"))
                val time = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Today,
                        contentDescription = null
                    )
                    Text(text = date)
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccessTime,
                        contentDescription = null
                    )
                    Text(text = time)
                }
            }
        }
    }
}
