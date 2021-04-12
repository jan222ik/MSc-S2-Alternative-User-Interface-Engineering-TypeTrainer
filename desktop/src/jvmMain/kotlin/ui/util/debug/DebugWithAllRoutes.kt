package ui.util.debug

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import textgen.error.ExerciseEvaluation
import textgen.generators.impl.RandomKnownWordGenerator
import ui.dashboard.ApplicationRoutes
import ui.exercise.ExerciseMode
import ui.exercise.TypingOptions
import ui.general.WindowRouterAmbient
import ui.util.i18n.LanguageDefinition
import kotlin.reflect.KClass


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DebugWithAllRoutes() {
    val router = WindowRouterAmbient.current
    LazyColumn {
        stickyHeader {
            Surface {
                Text("Routes:")
            }
        }
        items(getDebugPaths(ApplicationRoutes::class)) {
            when (it.objectInstance) {
                ApplicationRoutes.Dashboard,
                ApplicationRoutes.Settings,
                ApplicationRoutes.User.Login,
                ApplicationRoutes.Exercise.ExerciseSelection,
                //ApplicationRoutes.Exercise.Connection.QRCode,
                //ApplicationRoutes.Exercise.Connection.SetupInstructions,
                ApplicationRoutes.Goals.Overview,
                ApplicationRoutes.Goals.Compose,
                ApplicationRoutes.Achievements,
                ApplicationRoutes.Competitions.Overview,
                ApplicationRoutes.Debug,
                ApplicationRoutes.History -> {
                    val objectInstance = it.objectInstance!!
                    Button(onClick = { router.navTo(objectInstance) }) {
                        Text(text = +objectInstance.title)
                    }
                }
                null -> when (it) {
                    ApplicationRoutes.User.AccountManagement::class -> {
                        val dest = ApplicationRoutes.User.AccountManagement(Any())
                        Button(onClick = { router.navTo(dest) }) {
                            Text(text = +dest.title)
                        }
                    }
                    ApplicationRoutes.Exercise.Training::class -> {
                        val dest = ApplicationRoutes.Exercise.Training(
                            TypingOptions(
                                generatorOptions = RandomKnownWordGenerator.RandomKnownWordOptions(
                                    seed = 1L,
                                    minimalSegmentLength = 300,
                                    language = LanguageDefinition.German
                                ),
                                durationMillis = 1 * 60_000,
                                type = ExerciseMode.Speed,
                                isCameraEnabled = true
                            )
                        )
                        Button(onClick = { router.navTo(dest) }) {
                            Text(text = +dest.title)
                        }
                    }
                    ApplicationRoutes.Exercise.ExerciseResults::class -> {
                        val dest = ApplicationRoutes.Exercise.ExerciseResults(ExerciseEvaluation())
                        Button(onClick = { router.navTo(dest) }) {
                            Text(text = +dest.title)
                        }
                    }
                    else -> {
                        Text(text = "Missing path in debug for class $it")
                    }
                }
            }
        }
    }
}

private fun <T : Any> getDebugPaths(kClass: KClass<T>): List<KClass<out T>> {
    return when {
        kClass.isSealed -> kClass.sealedSubclasses.map { getDebugPaths(it) }.flatten()
        kClass.isData || kClass.objectInstance != null -> {
            return listOf(kClass)
        }
        else -> emptyList()
    }

}

