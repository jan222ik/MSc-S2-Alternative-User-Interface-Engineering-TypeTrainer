@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.exercise.practice.text

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
import com.github.jan222ik.desktop.ui.util.i18n.i18n

@Composable
fun BoxScope.TypingSetup() {
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
