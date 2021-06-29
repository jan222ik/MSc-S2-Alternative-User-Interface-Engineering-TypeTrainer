package ui.dashboard

import org.jetbrains.exposed.sql.transactions.transaction
import textgen.database.DbHistoryDAO
import java.time.LocalDate
import java.time.temporal.IsoFields
import kotlin.random.Random


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
            val localDates = DbHistoryDAO.all().map { it.timestamp.toLocalDate() }
            calcStreakFromDates(localDates, today())
        }
    }

    fun today(): LocalDate {
        return LocalDate.now()
//        return LocalDate.of(2020, 11, 30)
    }

    fun totalExercises(): Int{
        return transaction {
            DbHistoryDAO.all().count().toInt()
        }
    }

    fun getPractisesThisMonth(): List<LocalDate> {
        return transaction {
            DbHistoryDAO.all()
                .filter { it.timestamp.toLocalDate().monthValue == today().monthValue }
                .map { history -> history.timestamp.toLocalDate() }
                .distinct()
                .sortedBy { it }
        }
    }

    fun getPractisesThisMonthMock(): List<List<Pair<LocalDate, Boolean>>> {
        val mutableDays = mutableListOf<Pair<LocalDate, Boolean>>()
        for (i in 1..14) {
            mutableDays.add(Pair(LocalDate.of(2021, 4, i), Random.nextInt(100) <= 70))
        }
        for (i in 15..30) {
            mutableDays.add(Pair(LocalDate.of(2021, 4, i), false))
        }
        val days = mutableDays.toList()
        val test = days.groupBy { it.first.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) }
        return test.values.toList()
    }
}
