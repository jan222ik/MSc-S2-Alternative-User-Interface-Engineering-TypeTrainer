package ui.dashboard.content

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.dashboard.StreakAPI
import java.time.LocalDate
import java.time.temporal.ChronoField

var dayCounter = -1

@Composable
fun StreakCalendar() {
    dayCounter = today().dayOfMonth
    val streakApi = StreakAPI()
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val maxHeight = this.maxHeight
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            var practiseWeeks = streakApi.getPractisesThisMonth()
            var weeksDisplayed = 0
            while (practiseWeeks.isNotEmpty()) {
                val week = practiseWeeks.first()
                practiseWeeks = practiseWeeks.drop(1)
                CalendarWeek(maxHeight = maxHeight, practiceDays = week)
                weeksDisplayed += 1
            }

            while (weeksDisplayed < 6) {
                CalendarWeek(maxHeight = maxHeight, practiceDays = listOf())
                weeksDisplayed += 1
            }
        }
    }
}


@Composable
fun CalendarDay(modifier: Modifier, displayDate: Boolean, practised: Boolean) {
    var mod = modifier.border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(percent = 35))
    if (practised) {
        mod = modifier.background(MaterialTheme.colors.primary)
    }
    Box(modifier = mod, contentAlignment = Alignment.Center) {
        if (displayDate) {
            Text(text = today().dayOfMonth.toString(), color = Color.White)
        }
    }
}

fun today(): LocalDate {
    return LocalDate.now()
//    return LocalDate.of(2020, 11, 30)
}

@Composable
fun fillBoxes(n: Int, mod: Modifier) {
    var counter = n
    while (counter > 0) {
        Spacer(modifier = mod)
        counter -= 1
    }
}

@Composable
fun CalendarWeek(maxHeight: Dp, practiceDays: List<Pair<LocalDate, Boolean>>) {
    practiceDays.forEach { println("${it.first}  |  ${it.second}") }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        BoxWithConstraints {
            val minSpacer = 8.dp
            val cornerShape = RoundedCornerShape(percent = 35)
            val widthPerDay = this.maxWidth.div(7)
            val heightPerDay = maxHeight.div(6)
            val size = maxOf(10.dp, minOf(widthPerDay.minus(minSpacer), heightPerDay.minus(6.dp)))
            val spacer = this.maxWidth.minus(size.times(7)).div(8)
            val mod = Modifier
                .size(size)
                .clip(cornerShape)

            Row(horizontalArrangement = Arrangement.spacedBy(spacer)) {
                var spacersNeeded = 7 - practiceDays.size
                var isPastWeek = false
                if (practiceDays.isNotEmpty()) {
                    isPastWeek = practiceDays.first().first.get(ChronoField.ALIGNED_WEEK_OF_YEAR) <
                            today().get(ChronoField.ALIGNED_WEEK_OF_YEAR)
                }
                if (isPastWeek) {
                    fillBoxes(n = spacersNeeded, mod = mod)
                }
                practiceDays.forEach {
                    CalendarDay(
                        mod,
                        displayDate = dayCounter == 1,
                        practised = it.second
                    )
                    dayCounter -= 1
                }
                if (!isPastWeek) {
                    var index = 1L
                    while (spacersNeeded > 0) {
                        CalendarDay(mod, displayDate = dayCounter == 1, practised = false)
                        spacersNeeded -= 1
                        dayCounter -= 1
                        index += 1
                    }
                }
            }
        }
    }
}
