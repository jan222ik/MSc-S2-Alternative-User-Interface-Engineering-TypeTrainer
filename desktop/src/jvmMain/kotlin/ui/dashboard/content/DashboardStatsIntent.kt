package ui.dashboard.content

import com.github.jan222ik.compose_mpp_charts.core.data.DataPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jetbrains.exposed.sql.transactions.transaction
import textgen.database.DbHistory
import textgen.database.DbHistoryDAO
import textgen.database.DbHistorys
import textgen.database.getWeekly
import java.time.Duration
import java.time.LocalDate
import java.time.Period

class DashboardStatsIntent {
    private val mutableWeekData = MutableStateFlow<List<DataPoint>>(listOf())
    val weekData: StateFlow<List<DataPoint>>
        get() = mutableWeekData

    fun init(scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            mutableWeekData.emit(getWeekData())
        }
    }

    private fun getWeekData(): List<DataPoint> {
        val today = LocalDate.now()
        return DbHistorys
            .getWeekly()
            .groupBy { it.timestampDate }
            .map {
                val wpsAvg = it.value.sumByDouble { e -> e.data.value.wps.toDouble() }.div(it.value.size)
                it.key to wpsAvg
            }
            .sortedBy { it.first } // Sort needed other wise the lines that are drawn connect in the wrong order
            .map {
                val offsetFromToday = 6.minus(Period.between(it.first, today).days)
                DataPoint(x = offsetFromToday.toFloat(), it.second.times(60).toFloat())
            }
    }

    fun totalExercises(): Int {
        return transaction {
            DbHistoryDAO.count().toInt()
        }
    }

    fun streakDays(): Int {
        System.err.println("TODO get days of streak")
        return -1
    }
}
