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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ui.dashboard.StreakAPI
import ui.util.i18n.LanguageAmbient
import ui.util.i18n.i18n
import java.time.LocalDate

@Composable
fun CalendarWeek(from: LocalDate, maxDate: LocalDate, mod: Modifier, practiceDays: List<LocalDate>): LocalDate {
    var runningDate = from
    if (from.dayOfMonth == 1){
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            for (i in 1 until from.dayOfWeek.value) {
                Spacer(modifier = mod)
            }
            for (i in from.dayOfWeek.value..7) {
                Day(modifier = mod, day = runningDate.dayOfMonth, practised = practiceDays.contains(runningDate))
                runningDate = runningDate.plusDays(1)
            }
        }
    }
    else {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            for(i in 1..7){
                if (!runningDate.isAfter(maxDate)) {
                    Day(modifier = mod, day = runningDate.dayOfMonth, practised = practiceDays.contains(runningDate))
                }
                else{
                    Spacer(modifier = mod)
                }
                runningDate = runningDate.plusDays(1)
            }
        }
    }
    return runningDate
}

@Composable
fun StreakCalendar() {
    val streakApi = StreakAPI()
    val practises = streakApi.getPractisesThisMonth()
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val minSpacer = 8.dp
        val cornerShape = RoundedCornerShape(percent = 35)
        val widthPerDay = this.maxWidth.div(7)
        val heightPerDay = maxHeight.div(7)
        val size = maxOf(10.dp, minOf(widthPerDay.minus(minSpacer), heightPerDay.minus(6.dp)))
        this.maxWidth.minus(size.times(7)).div(8)
        val mod = Modifier
            .size(size)
            .clip(cornerShape)

        var runningDate = LocalDate.of(today().year, today().month, 1)
        val lastDay = runningDate.plusMonths(1).minusDays(1)
        Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.SpaceBetween) {
            WeekDayHeader(mod = mod)
            while(!runningDate.isAfter(lastDay)){
                runningDate = CalendarWeek(from = runningDate, maxDate = lastDay, mod = mod, practiceDays = practises)
            }
        }

    }
}

@Composable
fun WeekDayHeader(mod: Modifier){
    val weekdays = remember(LanguageAmbient.current.language) {
        i18n.str.dashboard.weeklyChart.daysOfWeek.resolve().split(" ")
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        for (day in weekdays){
            Box(modifier = mod, contentAlignment = Alignment.Center) {
                Text(text = day)
            }
        }
    }
}

@Composable
fun Day(modifier: Modifier, day: Int, practised: Boolean) {
    var borderWidth = 1.dp
    var fontWeight = FontWeight.Normal
    var mod = modifier

    if (today().dayOfMonth == day){
        borderWidth = 3.dp
        fontWeight = FontWeight.ExtraBold
    }

    mod = mod.border(width = borderWidth, color = Color.White, shape = RoundedCornerShape(percent = 35))
    if (practised) {
        mod = mod.background(MaterialTheme.colors.primary)
    }

    Box(modifier = mod, contentAlignment = Alignment.Center) {
        Text(text = day.toString(), color = Color.White, fontWeight = fontWeight)
    }
}

fun today() = LocalDate.now()
//fun today() = LocalDate.of(2021, 2, 10)