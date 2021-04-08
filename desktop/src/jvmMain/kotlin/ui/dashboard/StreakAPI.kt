package ui.dashboard

import org.jetbrains.exposed.sql.transactions.transaction
import textgen.database.DbHistory
import java.time.LocalDate


class StreakAPI {

    fun calcStreakFromDates(dates: List<LocalDate>, today: LocalDate): Int {
        val sorted = dates.sortedByDescending { localDate: LocalDate -> localDate }

        var prev = 1L
        var streak = 0
        var exercisedToday = false

        for (date in sorted) {
            if (today.equals(date) && !exercisedToday) {
                exercisedToday = true
                streak += 1
            } else if (today.minusDays(prev).equals(date)) {
                streak += 1
                prev += 1
            } else {
                break
            }
        }
        return streak
    }

    fun calcStreaks(): Int {
        return transaction {
            val localDates = DbHistory.all().map { it.timestampDate.value.toLocalDate() }
            calcStreakFromDates(localDates, LocalDate.now())
        }
    }

    fun totalExercises(): Int{
        return transaction {
            DbHistory.all().count().toInt()
        }
    }
}