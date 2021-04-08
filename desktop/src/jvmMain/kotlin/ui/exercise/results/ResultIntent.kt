package ui.exercise.results

import textgen.error.ExerciseEvaluation

class ResultIntent(
    private val data: ExerciseEvaluation
) {

    fun getKeyPoints(): List<KeyPoint> {
        return listOf(
            KeyPoint("Words typed", data.wordsTyped.toString()),
            KeyPoint("Characters typed", data.totalCharsTyped.toString()),
            KeyPoint("Accuracy", data.totalAccuracy.times(100).toString() + "%"),
            KeyPoint("Errors", data.totalErrors.toString()),
            KeyPoint("Error %", "2.5%"),
            KeyPoint("Typing Errors", data.falseCharsTyped.toString()),
            KeyPoint("Typing Errors %", "1.6%"),
            KeyPoint("Finger Errors", "4"),
            KeyPoint("Finger Errors %", "0.8%")
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
