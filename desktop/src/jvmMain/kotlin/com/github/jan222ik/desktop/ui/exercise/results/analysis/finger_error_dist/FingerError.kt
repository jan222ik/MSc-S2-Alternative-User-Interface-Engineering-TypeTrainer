@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.exercise.results.analysis.finger_error_dist

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.ui.components.TypeTrainerTheme
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import com.github.jan222ik.desktop.textgen.error.ExerciseEvaluation
import com.github.jan222ik.desktop.ui.util.i18n.LocalTranslationI18N


@Composable
fun LazyItemScope.FingerError(exerciseEvaluation: ExerciseEvaluation, isStandalone: Boolean) {
    val data = remember(exerciseEvaluation) { exerciseEvaluation.errorRateOfFingers }
    BaseDashboardCard(
        modifier = Modifier
            .fillParentMaxWidth(0.8f.takeIf { isStandalone } ?: 1f)
            .fillParentMaxHeight(0.8f)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = +LocalTranslationI18N(
                eng = "Errors per Fingers",
                ger = "Fehler nach Finger"
            ), style = MaterialTheme.typography.h5)

            Box {
                BoxWithConstraints(
                    modifier = Modifier.fillParentMaxWidth()
                ) {
                    if (data.isEmpty()) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = +LocalTranslationI18N(
                            eng = "No finger errors were detected or occurred",
                            ger = "Es sind keine Fehler mit Fingern erkannt worden."
                        )
                        )
                    } else {
                        FingerErrorChart(
                            chartData = data,
                            modifier = Modifier.size(this.maxWidth, this.maxHeight)
                        )
                    }
                }
                TextButton(
                    modifier = Modifier.align(Alignment.TopEnd).padding(5.dp),
                    onClick = {
                        println("Open other window btn clicked!")
                        Window(
                            title = "Chart: Errors per Fingers",
                            size = IntSize(width = 1920, height = 1080)
                        ) {
                            TypeTrainerTheme {
                                Surface(color = MaterialTheme.colors.background) {
                                    FingerErrorChart(chartData = data, modifier = Modifier.fillMaxSize())
                                }
                            }
                        }
                    }
                ) {
                    Text("Open in other Window")
                }
            }
        }
    }
}
