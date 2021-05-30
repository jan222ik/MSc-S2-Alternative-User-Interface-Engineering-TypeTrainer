package ui.exercise.results

import textgen.error.ExerciseEvaluation
import ui.exercise.results.overview.KeyPoint

class ResultIntent(
    internal val data: ExerciseEvaluation
) {

    fun getKeyPoints(): List<KeyPoint> {
        return listOf(
            KeyPoint("Words typed", data.wordsTyped.toString()),
            KeyPoint("Characters typed", data.totalCharsTyped.toString()),
            KeyPoint("Accuracy", data.totalAccuracy.times(100).toString() + "%"),
            KeyPoint("Errors", data.totalErrors.toString()),
            KeyPoint("Error %", "hc"),
            KeyPoint("Typing Errors", data.falseCharsTyped.toString()),
            KeyPoint("Typing Errors %", "hc"),
            KeyPoint("Finger Errors", "hc"),
            KeyPoint("Finger Errors %", "hc")
        )
    }

    companion object {

        fun getAchievements(): List<List<String>> {
            return listOf(
                listOf("The Marksman", "Complete a practise with a accuracy above 97%"),
                listOf("On a roll", "https://youtu.be/dQw4w9WgXcQ"),
                listOf("Lorem ipsum", "Achieved when the developer runs out of ideas")
            )
        }
    }
}
