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
import ui.util.i18n.i18n

@Composable
fun BoxScope.TypingSetup(intent: IPracticeIntend) {
    Box(
        modifier = Modifier
            .matchParentSize()
            .background(color = MaterialTheme.colors.background.copy(alpha = 0.8f))
            .padding(all = 5.dp),
    ) {
        Text(
            style = MaterialTheme.typography.h5,
            modifier = Modifier.align(Alignment.Center),
            text = +i18n.str.exercise.selection.controls.startingHint
        )
    }
}
