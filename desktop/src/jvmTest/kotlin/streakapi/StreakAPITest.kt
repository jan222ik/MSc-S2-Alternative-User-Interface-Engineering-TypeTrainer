package streakapi

import org.junit.jupiter.api.Test
import com.github.jan222ik.desktop.ui.dashboard.StreakAPI
import java.time.LocalDate
import kotlin.test.assertEquals

class StreakAPITest {

    val api = StreakAPI()

    @Test
    fun calcStreakExercisedToday(){
        val today = LocalDate.of(2021,4, 10)
        val dates = listOf<LocalDate>(
            LocalDate.of(2021, 4, 10),
            LocalDate.of(2021, 4, 9),
            LocalDate.of(2021, 4, 8),
            LocalDate.of(2021, 4, 7),
            LocalDate.of(2021, 4, 5),
            LocalDate.of(2021, 4, 4),
            LocalDate.of(2021, 4, 2),
        )
        assertEquals(4, api.calcStreakFromDates(dates, today))
    }

    @Test
    fun calcStreakNotExercisedToday(){
        val today = LocalDate.of(2021,4, 11)
        val dates = listOf<LocalDate>(
            LocalDate.of(2021, 4, 10),
            LocalDate.of(2021, 4, 9),
            LocalDate.of(2021, 4, 8),
            LocalDate.of(2021, 4, 7),
            LocalDate.of(2021, 4, 5),
            LocalDate.of(2021, 4, 4),
            LocalDate.of(2021, 4, 2),
        )
        assertEquals(4, api.calcStreakFromDates(dates, today))
    }

    @Test
    fun calcStreakLeapYearNotExercisedToday(){
        val today = LocalDate.of(2020,4, 11)
        val dates = listOf<LocalDate>(
            LocalDate.of(2020, 4, 10),
            LocalDate.of(2020, 4, 9),
            LocalDate.of(2020, 4, 8),
            LocalDate.of(2020, 4, 7),
            LocalDate.of(2020, 4, 5),
            LocalDate.of(2020, 4, 4),
            LocalDate.of(2020, 4, 2),
        )
        assertEquals(4, api.calcStreakFromDates(dates, today))
    }

    @Test
    fun calcStreakLeapYearExercisedToday(){
        val today = LocalDate.of(2020,4, 10)
        val dates = listOf<LocalDate>(
            LocalDate.of(2020, 4, 10),
            LocalDate.of(2020, 4, 9),
            LocalDate.of(2020, 4, 8),
            LocalDate.of(2020, 4, 7),
            LocalDate.of(2020, 4, 5),
            LocalDate.of(2020, 4, 4),
            LocalDate.of(2020, 4, 2),
        )
        assertEquals(4, api.calcStreakFromDates(dates, today))
    }

    @Test
    fun calcStreakOverlappingYears(){
        val today = LocalDate.of(2021,1, 3)
        val dates = listOf<LocalDate>(
            LocalDate.of(2021, 1, 2),
            LocalDate.of(2021, 1, 1),
            LocalDate.of(2020, 12, 31),
            LocalDate.of(2020, 12, 30),
            LocalDate.of(2020, 12, 28),
            LocalDate.of(2020, 12, 27),
        )
        assertEquals(4, api.calcStreakFromDates(dates, today))
    }

}
