@file:Suppress("FunctionName")

package _learn

import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.random.Random


@Composable
fun Counter(
    modifier: Modifier = Modifier
) {
    val (counter, setCounter) = remember { mutableStateOf(0) }
    val listColors = listOf(Color.Cyan, Color.White, Color.Magenta)
    val selectedColor = remember(counter) {
        val abs = abs(Random.nextInt())
        listColors[abs(counter) % listColors.size]
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .background(selectedColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround


    ) {
        Text(text = "Counter Value: $counter")
        Row {
            Button(
                onClick = {
                    setCounter(counter.dec())
                    println("Decrease")
                }
            ) {
                Text(text = "Decrease")
            }
            Spacer(Modifier.width(15.dp))
            Button(
                onClick = {
                    setCounter(counter.inc())
                    println("Increase")
                }
            ) {
                Text(text = "Increase")
            }
        }
        Text(
            modifier = Modifier
                .background(Color.Red)
                .padding(all = 16.dp)
                .background(Color.Magenta)
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .background(Color.Cyan),
            text = "Press"
        )
    }
}


fun main() {
    Window(size = IntSize(411, 600)) {
        Counter()
    }
}
