@file:Suppress("FunctionName")

package ui.exercise.practice

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ui.components.progress.practice.CountDownProgressBar
import ui.dashboard.BaseDashboardCard
import ui.exercise.ITypingOptions
import ui.util.debug.ifDebugCompose

@Composable
fun PracticeScreen(typingOptions: ITypingOptions) {
    /*
    val intend = PracticeIntendImpl(
        typingOptions = TypingOptions(
            generatorOptions = RandomKnownWordGenerator.RandomKnownWordOptions(
                seed = 1L,
                language = LanguageDefinition.German,
                minimalSegmentLength = 1000
            ),
            durationMillis = 60 * 1000,
            type = ExerciseMode.Speed
        )
    )
     */
    val intend = remember(typingOptions) { PracticeIntendImpl(typingOptions = typingOptions) }
    DisposableEffect(intend) {
        onDispose {
            intend.cancelRunningJobs()
        }
    }
    val localWindow = LocalAppWindow.current
    ifDebugCompose {
        DisposableEffect(intend) {
            var closeWindow: (() -> Unit)? = null
            PracticeScreenDebuggable(
                intend = intend as IDebugPracticeIntend,
                parentWindowLocation = IntOffset(localWindow.x, localWindow.y),
                parentWindowWidth = localWindow.width,
                parentWindowHeight = localWindow.height,
                windowClose = {
                    closeWindow = it
                    localWindow.events.onClose = it
                }
            )

            onDispose {
                closeWindow?.invoke()
            }
        }
    }
    PracticeScreenContent(intend)
}

@Composable
private fun PracticeScreenContent(intend: IPracticeIntend) {
    val max = intend.typingOptions.durationMillis.div(1000).toFloat()

    Column(
        modifier = Modifier.fillMaxWidth().padding(25.dp)
    ) {
        //progress bar
        val timer = intend.timerStateFlow.collectAsState()
        CountDownProgressBar(
            modifier = Modifier.fillMaxWidth(),
            value = max - timer.value.div(1000).toFloat(),
            max = max,
            trackColor = MaterialTheme.colors.background
        )
        // text in base dashboard card
        // TODO adjust to fit generated text -> all test on screen or just one row etc.
        BaseDashboardCard(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.7f)
                .padding(vertical = 8.dp)
                .fillMaxHeight(0.5f)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center).fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(50.dp),
                ) {
                    Text(text = "Generated text:")
                    val text = intend.textStateFlow.collectAsState()
                    Text(text = text.value)
                }
            }
        }
        if (intend.isCameraEnabled.value) {
            // collapsable video feed
            VideoFeedExpanding(title = "Video Feed")
        }
    }

}

/**
 * Expanding Card fo collapsable Video feed
 * TODO
 */
@Composable
private fun ColumnScope.VideoFeedExpanding(
    title: String,
    bgColor: Color = MaterialTheme.colors.background,
    shape: Shape = RoundedCornerShape(16.dp) //TODO change to fit bottom of screen
) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier
        .align(Alignment.CenterHorizontally)
        .fillMaxHeight()
    ) {
        Card(
            modifier = Modifier.align(Alignment.BottomCenter),
            shape = shape,
            backgroundColor = bgColor,
            elevation = 16.dp
        ) {
            Column(modifier = Modifier.padding(all = 5.dp)) {
                Text(text = title, textAlign = TextAlign.Center)
                if (expanded) {
                    //Text(text = body)
                    VideoFeedLive()
                    IconButton(onClick = { expanded = false }) {
                        Icon(Icons.Default.ExpandLess, contentDescription = "Collapse")
                    }
                } else {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.ExpandMore, contentDescription = "Expand")
                    }
                }
            }
        }
    }
}

@Composable
private fun VideoFeedLive() { //placeholder for actual live feed
    Box(
        modifier = Modifier.fillMaxWidth().padding(50.dp)
            .background(Color.DarkGray)
    ) {
        Text(
            modifier = Modifier.padding(50.dp),
            text = "Live Feed Placeholder",
            color = Color.LightGray
        )
    }
}



