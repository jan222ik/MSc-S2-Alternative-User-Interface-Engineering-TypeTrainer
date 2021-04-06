@file:Suppress("FunctionName")

package ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.jetbrains.exposed.sql.transactions.transaction
import textgen.database.DbHistory
import ui.dashboard.BaseDashboardCard

@Composable
fun HistoryScreen() {
    val items = remember { transaction { DbHistory.all().toList() } }
    val currItem = remember(items) { mutableStateOf(0) }
    Row(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .fillMaxHeight()
        ) {
            if (items.isNotEmpty()) {
                itemsIndexed(items) { index, item ->
                    Card(
                        modifier = Modifier
                            .clickable {
                                currItem.value = index
                            },
                        backgroundColor = when (currItem.value) {
                            index -> MaterialTheme.colors.primary
                            else -> MaterialTheme.colors.surface
                        }
                    ) {
                        Text("$index ${item.timestampDate.value}")
                    }
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (items.isNotEmpty()) {
                val item = remember(currItem.value, items) { items[currItem.value] }
                BaseDashboardCard {
                    Column {
                        Text("histId:" + item.histId.value)
                        Text("timestamp:" + item.timestampDate.value)
                        Text("dataJson:" + item.dataJson)
                        Text("data:" + item.data.value)
                    }
                }
            } else {
                Text("Complete a practice to see your results here!")
            }
        }
    }
}

