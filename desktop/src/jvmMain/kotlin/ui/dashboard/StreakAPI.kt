package ui.dashboard

import org.jetbrains.exposed.sql.transactions.transaction
import textgen.database.DbHistory
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
            val localDates = DbHistory.all().map { it.timestampDate.value.toLocalDate() }
            calcStreakFromDates(localDates, today())
        }
    }

    fun today(): LocalDate {
        return LocalDate.now()
//        return LocalDate.of(2020, 11, 30)
    }

    fun totalExercises(): Int{
        return transaction {
            DbHistory.all().count().toInt()
        }
    }

    fun getPractisesThisMonth(): List<List<Pair<LocalDate, Boolean>>>{
        return transaction {
            val histories = DbHistory.all()
                .filter { it.timestampDate.value.toLocalDate().monthValue == today().monthValue }
                .map { history -> history.timestampDate.value.toLocalDate() }
                .distinct()
                .sortedBy { it }
            //histories.forEach { println(it) }
            val result = mutableListOf<Pair<LocalDate, Boolean>>()
            var date = LocalDate.of(today().year, today().monthValue, 1)

            var index = 0
            while(date.monthValue == today().monthValue){
                if(index < histories.size && date == histories.get(index)){
                    result.add(Pair(date, true))
                    index += 1
                }
                else{
                    result.add(Pair(date, false))
                }
                date = date.plusDays(1L)
            }
            //result.forEach { println("${it.first}: ${it.second}") }
            result.groupBy { it.first.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) }.values.toList()
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
