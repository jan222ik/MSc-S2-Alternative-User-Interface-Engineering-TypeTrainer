@file:Suppress("FunctionName")

package ui.exercise.practice.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.exercise.practice.IPracticeIntend
import ui.util.i18n.RequiresTranslationI18N

@Composable
fun BoxScope.TypingSetup(intend: IPracticeIntend) {
    Box(
        modifier = Modifier
            .matchParentSize()
            .background(color = MaterialTheme.colors.background.copy(alpha = 0.8f))
            .padding(all = 5.dp),
    ) {
        if (intend.isCameraEnabled.value) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = +RequiresTranslationI18N(
                    "Please get into calibrating position by placing your left hand pinky finger on the key " +
                            "beneath ESC and your right hand pinky finger on BACKSPACE.\n" +
                            "Press SPACE to start your exercise in this position!"
                )
            )
        } else {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = +RequiresTranslationI18N("Start typing whenever you are ready!")
            )
        }
    }
}
